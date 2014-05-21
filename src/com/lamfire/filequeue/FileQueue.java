package com.lamfire.filequeue;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lamfire.logger.Logger;
import com.lamfire.utils.Lists;
import com.lamfire.utils.Maps;

/**
 * 一个高性能文件对列,支持先进先出
 * 
 * @author lamfire
 * 
 */
public class FileQueue {
	private static final Logger LOGGER = Logger.getLogger(FileQueue.class);
	private static final int DEFAULT_BUFFER_SIZE = 4 * 1024 * 1024;
	private int indexBufferSize = DEFAULT_BUFFER_SIZE;
	private int storeBufferSize = DEFAULT_BUFFER_SIZE;

	private final Lock lock = new ReentrantLock();

	private String dir;
	private String name;

	private final MetaIO meta;
	private final List<IndexIO> indexs = Lists.newArrayList();
	private final Map<Integer, StoreIO> stores = Maps.newHashMap();

	public FileQueue(String dirPath, String name) throws IOException {
		this(dirPath, name, DEFAULT_BUFFER_SIZE, DEFAULT_BUFFER_SIZE);
	}

	public FileQueue(String dirPath, String name, int storeBufferSize) throws IOException {
		this(dirPath, name, DEFAULT_BUFFER_SIZE, storeBufferSize);
	}

	public FileQueue(String dirPath, String name, int pageBufferSize, int storeBufferSize) throws IOException {
		this.indexBufferSize = pageBufferSize;
		this.storeBufferSize = storeBufferSize;
		this.dir = dirPath;
		this.name = name;
		File dir = new File(dirPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		this.meta = new MetaIO(new File(dir.getAbsolutePath() + File.separator + name + MetaIO.META_FILE_SUFFIX));
        if(LOGGER.isDebugEnabled()){
		    LOGGER.debug("[QUEUE-META]:"+ dir.getAbsolutePath() + "" + name +" = " +meta.toString());
        }
		initialize();
	}

	void initialize() throws IOException {
		int pageCount = this.meta.getIndexCount();
		// add pages and stores
		for (int i = 0; i <= pageCount; i++) {
			// page
			File pageFile = IndexIO.getIndexFile(dir, name, i);
			IndexIO page = new IndexIO(pageFile, i, indexBufferSize);
			indexs.add(page);
		}
	}

	private IndexIO getWriteIndexIO() throws IOException {
		IndexIO io = indexs.get(indexs.size() - 1);
		if (io.getFreeElementSpace() <= 1) {
			int page = meta.incIndexCount();
			meta.setWriteOffset(0);

			File pageFile = IndexIO.getIndexFile(dir, name, page);
			io = new IndexIO(pageFile, page, indexBufferSize);
			io.setWriteOffset(0);
			indexs.add(io);
		}
		io.setWriteOffset(meta.getWriteOffset());
		return io;
	}

	private StoreIO getStoreIO(int index) throws IOException {
		StoreIO io = stores.get(index);
		if (io != null) {
			return io;
		}

		File file = StoreIO.getStoreFile(dir, name, index);
		io = new StoreIO(file, index, storeBufferSize);
		io.setWriteOffset(0);
		io.setReadOffset(0);
		stores.put(index, io);
		return io;
	}

	private  StoreIO getLastWriteStoreIO() throws IOException {
		StoreIO io = getStoreIO(meta.getWriteStore());
		io.setWriteOffset(meta.getWriteStoreOffset());
		return io;
	}

	private  IndexIO getReadIndexIO() throws IOException {
		int page = meta.getReadIndex();
		int readOffset = meta.getReadOffset();
		if ((readOffset + Element.ELEMENT_LENGTH) >= FileBuffer.MAX_FILE_LENGTH) {
			meta.incReadIndex();
			meta.setReadOffset(0);
			meta.flush();
			page++;
		}
		IndexIO pageIO = indexs.get(page);
		pageIO.setReadOffset(meta.getReadOffset());
		return pageIO;
	}

	public boolean add(byte[] bytes) {
		try {
			lock.lock();
			StoreIO io = getLastWriteStoreIO();
			int store = meta.getWriteStore();
			int writeOffset = meta.getWriteStoreOffset();

			if ((writeOffset + bytes.length) > FileBuffer.MAX_FILE_LENGTH) { // new
												// store
												// file
				store++;
				io = getStoreIO(store);
				io.setWriteOffset(0);
				meta.setWriteStore(store);
				meta.setWriteStoreOffset(0);
				meta.flush();
			}

			int offset = io.getWriteOffset();
			int length = bytes.length;

			io.write(bytes);

			Element e = new Element(store, offset, length);
			IndexIO pageIO = getWriteIndexIO();
			pageIO.add(e);

			meta.setWriteOffset(pageIO.getWriteOffset());
			meta.setWriteIndex(pageIO.getIndex());
			meta.setWriteStore(store);
			meta.setWriteStoreOffset(io.getWriteOffset());
			meta.flush();
			return true;
		} catch (IOException e) {
			throw new IOError(e);
		} finally {
			lock.unlock();
		}
	}

	public  boolean isEmpty() {
		return size() == 0;
	}

	public  byte[] peek() {
		if (isEmpty()) {
			return null;
		}
		try {
			lock.lock();

			IndexIO indexIO = getReadIndexIO();
			Element e = indexIO.take();

			StoreIO storeIO = getStoreIO(e.getStore());
			byte[] bytes = new byte[e.getLength()];
			storeIO.read(e.getPosition(), bytes);

			return bytes;
		} catch (IOException e) {
			throw new IOError(e);
		} finally {
			lock.unlock();
		}
	}

	public  long size() {
		try {
			lock.lock();
			return meta.getWritedCount() - meta.getReadedCount();
		} finally {
			lock.unlock();
		}
	}

	public byte[] poll() {
		if (isEmpty()) {
			return null;
		}

		try {
			lock.lock();

			byte[] bytes = peek();
			meta.setReadOffset(meta.getReadOffset() + Element.ELEMENT_LENGTH);
			meta.flush();
			return bytes;
		} catch (IOException e) {
			throw new IOError(e);
		} finally {
			lock.unlock();
		}
	}

	private void realseIndexs() {
		if (indexs.isEmpty()) {
			return;
		}
		for (IndexIO io : indexs) {
			io.close();
		}
		indexs.clear();
	}

	private void realseStores() {
		if (stores.isEmpty()) {
			return;
		}
		for (StoreIO io : stores.values()) {
			io.close();
		}
		stores.clear();
	}

	public void clear() {
		try {
			lock.lock();
			meta.clear();
			realseStores();
			realseIndexs();
            initialize();
		} catch (IOException e) {
			throw new IOError(e);
		} finally {
			lock.unlock();
		}
	}

	public void close() {
		try {
			lock.lock();
			realseStores();
			realseIndexs();
			this.meta.close();
		} finally {
			lock.unlock();
		}
	}

}

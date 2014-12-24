package com.lamfire.filequeue;

import com.lamfire.logger.Logger;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 一个高性能持久化列表，不支持删除元素操作
 * 
 * @author lamfire
 * 
 */
class FileListImpl implements FileList{
	private static final Logger LOGGER = Logger.getLogger(FileListImpl.class);
	private static final int DEFAULT_BUFFER_SIZE = 4 * 1024 * 1024;

	private int indexBufferSize = DEFAULT_BUFFER_SIZE;
	private int storeBufferSize = DEFAULT_BUFFER_SIZE;

	private final Lock lock = new ReentrantLock();

	private String dir;
	private String name;

	private MetaBuffer meta;
    private IndexManager indexMgr;
    private DataManager dataMgr;

    private Reader reader;
    private Writer writer;

    private int indexOfLastDeleteStoreFile = 0 ;
    private int indexOfLastDeleteIndexFile = 0;

	public FileListImpl(String dataDir, String name) throws IOException {
		this(dataDir, name, DEFAULT_BUFFER_SIZE, DEFAULT_BUFFER_SIZE);
	}

	public FileListImpl(String dataDir, String name, int storeBufferSize) throws IOException {
		this(dataDir, name, DEFAULT_BUFFER_SIZE, storeBufferSize);
	}

	public FileListImpl(String dataDir, String name, int indexBufferSize, int storeBufferSize) throws IOException {
		this.indexBufferSize = indexBufferSize;
		this.storeBufferSize = storeBufferSize;
		this.dir = dataDir;
		this.name = name;
		File dir = new File(dataDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		initialize();
	}

	void initialize() throws IOException {
        if(meta != null){
            return;
        }
        try {
            lock.lock();
            meta = new MetaBuffer(MetaBuffer.getMetaFile(dir, name));
            indexMgr = new IndexManager(this.meta,dir,name);
            dataMgr = new DataManager(this.meta,dir,name);
            reader = new ReaderImpl(this.meta,indexMgr, dataMgr) ;
            writer = new WriterImpl(this.meta,indexMgr, dataMgr);

            indexMgr.setBufferSize(indexBufferSize);
            dataMgr.setBufferSize(storeBufferSize);

            indexOfLastDeleteStoreFile = 0 ;
            indexOfLastDeleteIndexFile = 0;
        } finally {
            lock.unlock();
        }
	}



	public boolean add(byte[] bytes) {
		try {
			lock.lock();
            writer.write(bytes);
			return true;
		} catch (Exception e) {
			throw new IOError(e);
		} finally {
			lock.unlock();
		}
	}

	public  boolean isEmpty() {
		return size() == 0;
	}

    public  byte[] get(int i) {
        if (isEmpty()) {
            return null;
        }
        try {
            lock.lock();
            reader.moveTo(i);
            return reader.read();
        } catch (IOException e) {
            throw new IOError(e);
        } finally {
            lock.unlock();
        }
    }

	public  long size() {
		try {
			lock.lock();
			return meta.getWriteCount() - meta.getReadCount();
		} finally {
			lock.unlock();
		}
	}

	public void clear() {
		try {
			lock.lock();
            deleteAllIndexFiles();
            deleteAllDataFiles();
            meta.clear();
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
            dataMgr.close();
            indexMgr.close();
            meta.close();
		} finally {
			lock.unlock();
		}
	}

    public void delete(){
        try {
            lock.lock();
            meta.clear();
            deleteAllIndexFiles();
            deleteAllDataFiles();
            meta.closeAndDeleteFile();
        } catch (IOException e) {
            throw new IOError(e);
        } finally {
            lock.unlock();
        }
    }

    private void deleteAllDataFiles(){
        int store = meta.getWriteDataIndex();
        for(int i=indexOfLastDeleteStoreFile;i<=store;i++){
            dataMgr.deleteDataFile(i);
            indexOfLastDeleteStoreFile = i;
        }
    }

    private void deleteAllIndexFiles(){
        int index = meta.getWriteIndex();
        for(int i=indexOfLastDeleteIndexFile;i<=index;i++){
            indexMgr.deleteIndexFile(i);
            indexOfLastDeleteIndexFile = i;
        }
    }

}

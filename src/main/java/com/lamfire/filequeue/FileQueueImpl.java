package com.lamfire.filequeue;

import com.lamfire.logger.Logger;
import com.lamfire.utils.Threads;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 一个高性能持久化对列,支持先进先出
 * 
 * @author lamfire
 * 
 */
class FileQueueImpl implements FileQueue{
	private static final Logger LOGGER = Logger.getLogger(FileQueueImpl.class);
	private static final int DEFAULT_BUFFER_SIZE = 4 * 1024 * 1024;
    private static final int AUTO_CLEAR_INTERVAL  = 300; //s
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

	public FileQueueImpl(String dataDir, String name) throws IOException {
		this(dataDir, name, DEFAULT_BUFFER_SIZE, DEFAULT_BUFFER_SIZE);
	}

	public FileQueueImpl(String dataDir, String name, int storeBufferSize) throws IOException {
		this(dataDir, name, DEFAULT_BUFFER_SIZE, storeBufferSize);
	}

	public FileQueueImpl(String dataDir, String name, int indexBufferSize, int storeBufferSize) throws IOException {
		this.indexBufferSize = indexBufferSize;
		this.storeBufferSize = storeBufferSize;
		this.dir = dataDir;
		this.name = name;
		File dir = new File(dataDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		initialize();
        Threads.scheduleWithFixedDelay(autoClearExpiredFileMonitor,AUTO_CLEAR_INTERVAL,AUTO_CLEAR_INTERVAL, TimeUnit.SECONDS);
	}

	void initialize() throws IOException {
        try {
            lock.lock();
            if(meta == null){
                meta = new MetaBuffer(MetaBuffer.getMetaFile(dir, name));
            }

            if(indexMgr == null){
                indexMgr = new IndexManager(this.meta,dir,name);
            }

            if(dataMgr == null){
                dataMgr = new DataManager(this.meta,dir,name);
            }

            if(reader == null){
                reader = new ReaderImpl(this.meta,indexMgr, dataMgr) ;
            }

            if(writer == null){
                writer = new WriterImpl(this.meta,indexMgr, dataMgr);
            }

            indexMgr.setBufferSize(indexBufferSize);
            dataMgr.setBufferSize(storeBufferSize);

            indexOfLastDeleteStoreFile = 0 ;
            indexOfLastDeleteIndexFile = 0;
        } finally {
            lock.unlock();
        }
	}



	public boolean push(byte[] bytes) {
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

    public boolean push(byte[] bytes,int offset,int length) {
        try {
            lock.lock();
            writer.write(bytes,offset,length);
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



    public  byte[] peek() {
		return peek(0);
	}

    public  byte[] peek(int i) {
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

    public  byte[] pull() {
        if (isEmpty()) {
            return null;
        }
        try {
            lock.lock();
            if(reader.hashMore()){
                reader.moveTo(0);
                byte[] bytes = reader.read();
                reader.moveNext();
                reader.commit();
                return bytes;
            }
            return null;
        } catch (IOException e) {
            throw new IOError(e);
        } finally {
            lock.unlock();
        }
    }

    public void skip(int number){
        if (isEmpty() || number <= 0) {
            return ;
        }
        try {
            lock.lock();
            int i=0;
            while(reader.hashMore()){
                reader.moveNext();
                i++;
                if(i >= number){
                    break;
                }
            }
            reader.commit();
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
            this.reader = null;
            this.writer = null;
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
            Threads.removeScheduledTask(autoClearExpiredFileMonitor);
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
            Threads.removeScheduledTask(autoClearExpiredFileMonitor);
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

    private void deleteExpiredDataFiles(){
        int store = meta.getReadDataIndex();
        for(int i=indexOfLastDeleteStoreFile;i<store;i++){
            dataMgr.deleteDataFile(i);
            indexOfLastDeleteStoreFile = i;
        }
    }

    private void deleteExpiredIndexFiles(){
        int index = meta.getReadIndex();
        for(int i=indexOfLastDeleteIndexFile;i<index;i++){
            indexMgr.deleteIndexFile(i);
            indexOfLastDeleteIndexFile = i;
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

    Runnable autoClearExpiredFileMonitor = new Runnable() {
        @Override
        public void run() {
            try{
                lock.lock();
                deleteExpiredDataFiles();
                deleteExpiredIndexFiles();
                if(size() == 0 && meta.getWriteIndex() > 0){
                    clear();
                }
            }catch (Throwable t){

            } finally {
                lock.unlock();
            }
        }
    } ;
}

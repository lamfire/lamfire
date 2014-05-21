package com.lamfire.filequeue;

import java.io.File;
import java.io.IOException;

import com.lamfire.utils.Bytes;

/**
 * 队列元数据读写
 * @author lamfire
 *
 */
class MetaIO {
	public static final String META_FILE_SUFFIX = ".meta";
	public static final int META_FILE_LENGTH = 32;
	
	private FileBuffer file=null;  
	
	private final byte[] buffer = new byte[META_FILE_LENGTH];
	
	int indexCount = 0; //页总数

	int readIndex = 0; //当前读取页
	int readOffset = 0;//当前读取位置

	int writeIndex = 0;//当前写页
	int writeOffset = 0;//当前写位置
	
	int writeStore = 0; //最后写入Store 号
	int writeStoreOffset = 0;//最后写入数据位置
	
	public MetaIO(File file) throws IOException{
		this.file = new FileBuffer(file,META_FILE_LENGTH);
		reload();
	}
	
	private void reload()throws IOException{
		this.indexCount = file.getInt(0);

		this.readIndex = file.getInt(4);
		this.readOffset = file.getInt(8);

		this.writeIndex = file.getInt(12);
		this.writeOffset = file.getInt(16);
		
		this.writeStore = file.getInt(20);
		this.writeStoreOffset = file.getInt(24);
	}
	
	public void flush()throws IOException{
		Bytes.putInt(buffer,0, indexCount);
		Bytes.putInt(buffer,4, readIndex);
		Bytes.putInt(buffer,8, readOffset);
		Bytes.putInt(buffer,12, writeIndex);
		Bytes.putInt(buffer,16,writeOffset);
		Bytes.putInt(buffer,20,writeStore);
		Bytes.putInt(buffer,24,writeStoreOffset);
		file.put(0,buffer);
	}
	
	public void clear()throws IOException{
		Bytes.putInt(buffer,0, 0);
		Bytes.putInt(buffer,4, 0);
		Bytes.putInt(buffer,8, 0);
		Bytes.putInt(buffer,12, 0);
		Bytes.putInt(buffer,16, 0);
		Bytes.putInt(buffer,20, 0);
		Bytes.putInt(buffer,24,0);
		Bytes.putInt(buffer,28,0);
		Bytes.putInt(buffer,32,0);
		file.put(0,buffer);
	}
	
	public void close(){
		this.file.close();
		this.file = null;
	}

	
	public synchronized int incIndexCount()throws IOException{
		this.indexCount ++;
		return this.indexCount;
	}
	
	public synchronized void setPageCount(int pageCount)throws IOException{
		this.indexCount =pageCount;
	}

	public synchronized void incReadIndex()throws IOException{
		this.readIndex ++;
	}
	
	public synchronized void setReadIndex(int readPage)throws IOException{
		this.readIndex = readPage;
	}
	
	public synchronized void setReadOffset(int offset)throws IOException{
		this.readOffset = offset;
	}

	public synchronized void incWriteIndex()throws IOException{
		this.writeIndex ++;
	}
	
	public synchronized void setWriteIndex(int writePage)throws IOException{
		this.writeIndex = writePage;
	}
	
	public synchronized void setWriteOffset(int writeOffset)throws IOException {
		this.writeOffset = writeOffset;
	}
	
	public synchronized void setWriteStore(int lastWriteStore) throws IOException{
		this.writeStore = lastWriteStore;
	}

	public synchronized void setWriteStoreOffset(int lastWriteDataOffset) throws IOException{
		this.writeStoreOffset = lastWriteDataOffset;
	}

	public int getIndexCount() {
		return indexCount;
	}

	public long getReadedCount() {
        return (1l * FileBuffer.MAX_FILE_LENGTH * readIndex + readOffset) /  Element.ELEMENT_LENGTH;
	}

	public int getReadIndex() {
		return readIndex;
	}

	public int getReadOffset() {
		return readOffset;
	}

	public long getWritedCount() {
		return (1l * FileBuffer.MAX_FILE_LENGTH * writeIndex + writeOffset) /  Element.ELEMENT_LENGTH;
	}

	public int getWriteIndex() {
		return writeIndex;
	}

	public int getWriteOffset() {
		return writeOffset;
	}

	public int getWriteStore() {
		return writeStore;
	}
	
	public int getWriteStoreOffset() {
		return writeStoreOffset;
	}

    @Override
    public String toString() {
        return "MetaIO{" +
                "writeCount=" + getWritedCount() +
                "readCount=" + getReadedCount() +
                "indexCount=" + indexCount +
                ", writeStoreOffset=" + writeStoreOffset +
                ", writeStore=" + writeStore +
                ", writeOffset=" + writeOffset +
                ", writeIndex=" + writeIndex +
                ", readOffset=" + readOffset +
                ", readIndex=" + readIndex +
                '}';
    }
}

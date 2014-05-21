package com.lamfire.filequeue;

import java.io.File;
import java.io.IOException;

/**
 * 队列数据文件读写
 * @author lamfire
 *
 */
class StoreIO {
	public static final String FILE_SUFFIX = ".data";
	
	public static File getStoreFile(String dir,String name,int page){
		if(page ==0){
			return new File(dir+ File.separator +name + FILE_SUFFIX);
		}
		return new File(dir+ File.separator +name + FILE_SUFFIX +"." + page);
	}

	FileBuffer buffer;
	int store;
	
	public StoreIO(File file,int store) throws IOException {
		this.buffer = new FileBuffer(file);
		this.store = store;
	}

	public StoreIO(File file,int store,int bufferSize) throws IOException {
		this.buffer = new FileBuffer(file,bufferSize);
		this.store = store;
	}

	public void setWriteOffset(int offset) {
		this.buffer.setWritePostion(offset);
	}
	
	public void setReadOffset(int offset) {
		this.buffer.setReadPostion(offset);
	}

	public synchronized void write(byte[] bytes) throws IOException {
		this.buffer.put(bytes);		
	}
	
	public synchronized void read(byte[] bytes)throws IOException{
		this.buffer.get(bytes);
	}
	
	public synchronized void read(int postion ,byte[] bytes)throws IOException{
		this.buffer.setReadPostion(postion);
		read(bytes);
	}

	public void close() {
		this.buffer.close();
		this.buffer = null;
	}

    public void closeAndDeleteFile(){
        this.buffer.closeAndDeleteFile();
        this.buffer = null;
    }


	public int getWriteOffset() {
		return this.buffer.getWritePostion();
	}
	
	public int getReadOffset() {
		return this.buffer.getReadPostion();
	}
	
	public int getFreeWriteSpace(){
		return this.buffer.getFreeWriteSpace();
	}

	public int getStore() {
		return store;
	}
	
	
}

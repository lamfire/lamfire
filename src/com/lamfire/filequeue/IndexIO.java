package com.lamfire.filequeue;

import java.io.File;
import java.io.IOException;

/**
 * 队列索引文件读写
 * @author lamfire
 *
 */
class IndexIO {
	public static final String FILE_SUFFIX = ".idx";
	public static final int ELEMENT_LENGTH = Element.ELEMENT_LENGTH;

	public static File getIndexFile(String dir,String name,int page){
		if(page ==0){
			return new File(dir+ File.separator +name + FILE_SUFFIX);
		}
		return new File(dir+ File.separator +name + FILE_SUFFIX +"." + page);
	}

	FileBuffer buffer;
	int index = 0;
	
	public IndexIO(File file,int page) throws IOException {
		this.buffer = new FileBuffer(file);
		this.index = page;
	}
	
	public IndexIO(File file,int page,int bufferSize) throws IOException {
		this.buffer = new FileBuffer(file,bufferSize);
		this.index = page;
	}
	

	public void setWriteOffset(int offset) {
		this.buffer.setWritePostion(offset);
	}
	
	public void setReadOffset(int offset) {
		this.buffer.setReadPostion(offset);
	}

	public void add(Element element) throws IOException {
		this.buffer.put(element.asBytes());	
	}
	
	public synchronized Element take()throws IOException{
		byte[] bytes = new byte[ELEMENT_LENGTH];
		this.buffer.get(bytes);
		return Element.fromBytes(bytes);
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
	
	public int getFreeElementSpace(){
		return this.buffer.getFreeWriteSpace() / ELEMENT_LENGTH;
	}


	public int getIndex() {
		return index;
	}
	
	
}

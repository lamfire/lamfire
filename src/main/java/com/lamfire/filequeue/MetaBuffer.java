package com.lamfire.filequeue;

import com.lamfire.code.MD5;
import com.lamfire.utils.Bytes;
import com.lamfire.utils.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 队列元数据读写
 * @author lamfire
 *
 */
class MetaBuffer {
	public static final String FILE_SUFFIX = ".m";
	public static final int META_FILE_LENGTH = 32;

    public static String getMetaFileName(String dir,String name){
        dir = FilenameUtils.normalizeNoEndSeparator(dir);
        String fileName = MD5.hash(name + FILE_SUFFIX );
        return (dir+ File.separator + fileName + FILE_SUFFIX);
    }

    public static File getMetaFile(String dir,String name){
        return new File(getMetaFileName(dir, name));
    }

    public static void deleteMetaFile(String dir,String name){
        File file = getMetaFile(dir, name);
        if(file.exists() && file.isFile()){
            file.delete();
        }
    }
	
	private FileBuffer file = null;
	
	private final byte[] buffer = new byte[META_FILE_LENGTH];

    private final AtomicInteger readIndex = new AtomicInteger(0); //当前读取页
    private int readIndexOffset = 0;//当前读取位置

    private int readDataIndex = 0;
    private int readDataOffset = 0;

    private final AtomicInteger writeIndex = new AtomicInteger(0);//当前写页
    private int writeIndexOffset = 0;//当前写位置

    private final AtomicInteger writeDataIndex = new AtomicInteger(0); //最后写入Store 号
    private int writeDataOffset = 0;//最后写入数据位置
	
	public MetaBuffer(File file) throws IOException{
		this.file = new FileBuffer(file,META_FILE_LENGTH);
		reload();
	}
	
	private void reload()throws IOException{
		this.readIndex.set(file.getInt(0));
		this.readIndexOffset = file.getInt(4);
        this.readDataIndex = file.getInt(8);
        this.readDataOffset = file.getInt(12);

		this.writeIndex.set(file.getInt(16));
		this.writeIndexOffset = file.getInt(20);
		this.writeDataIndex.set(file.getInt(24));
		this.writeDataOffset = file.getInt(28);
	}
	
	public void flush()throws IOException{

		Bytes.putInt(buffer,0, readIndex.get());
		Bytes.putInt(buffer,4, readIndexOffset);
        Bytes.putInt(buffer,8, readDataIndex);
        Bytes.putInt(buffer,12, readDataOffset);

		Bytes.putInt(buffer,16, writeIndex.get());
		Bytes.putInt(buffer,20,writeIndexOffset);
		Bytes.putInt(buffer,24, writeDataIndex.get());
		Bytes.putInt(buffer,28, writeDataOffset);
		file.put(0,buffer);
	}
	
	public void clear()throws IOException{
        this.readIndex.set(0);
        this.readIndexOffset =0;
        this.readDataIndex = 0;
        this.readDataOffset = 0;

        this.writeIndex.set(0);
        this.writeIndexOffset = 0;
        this.writeDataIndex.set(0);
        this.writeDataOffset = 0;
        flush();
	}
	
	public void close(){
        if(this.file == null){
            return;
        }
		this.file.close();
		this.file = null;
	}

    public void closeAndDeleteFile(){
        if(this.file == null){
            return;
        }
        this.file.closeAndDeleteFile();
        this.file = null;
    }

	public long getReadCount() {
        return (1l * FileBuffer.MAX_FILE_LENGTH * readIndex.get() + readIndexOffset) /  Element.ELEMENT_LENGTH;
	}


	public long getWriteCount() {
		return (1l * FileBuffer.MAX_FILE_LENGTH * writeIndex.get() + writeIndexOffset) /  Element.ELEMENT_LENGTH;
	}

    public int getReadIndex() {
        return readIndex.get();
    }

    public void setReadIndex(int index){
        readIndex.set(index);
    }

    public synchronized void moveToNextReadIndex()throws IOException{
        int index = readIndex.incrementAndGet();
        if(index > writeIndex.get() ){
            readIndex.decrementAndGet();
            throw new IOException("Not found index : " + index);
        }
        setReadIndexOffset(0);
    }

    public synchronized void moveToNextWriteIndex() {
        writeIndex.incrementAndGet();
        setWriteIndexOffset(0);
    }

    public synchronized void moveToNextDataWriteIndex() {
        writeDataIndex.incrementAndGet();
        setWriteDataOffset(0);
    }

    public int getReadIndexOffset() {
        return readIndexOffset;
    }

    public void setReadIndexOffset(int readIndexOffset) {
        this.readIndexOffset = readIndexOffset;
    }

    public int getReadDataIndex() {
        return readDataIndex;
    }

    public void setReadDataIndex(int readStore) {
        this.readDataIndex = readStore;
    }

    public int getReadDataOffset() {
        return readDataOffset;
    }

    public void setReadDataOffset(int readDataOffset) {
        this.readDataOffset = readDataOffset;
    }

    public int getWriteIndex() {
        return writeIndex.get();
    }

    public void setWriteIndex0(int writeIndex) {
        this.writeIndex.set(writeIndex);
    }

    public int getWriteIndexOffset() {
        return writeIndexOffset;
    }

    public void setWriteIndexOffset(int writeIndexOffset) {
        this.writeIndexOffset = writeIndexOffset;
    }

    public int getWriteDataIndex() {
        return writeDataIndex.get();
    }

    public void setWriteDataIndex0(int writeDataIndex) {
        this.writeDataIndex.set(writeDataIndex);
    }

    public int getWriteDataOffset() {
        return writeDataOffset;
    }

    public void setWriteDataOffset(int writeDataOffset) {
        this.writeDataOffset = writeDataOffset;
    }

    @Override
    public String toString() {
        return "MetaIO{" +
                "writeCount=" + getWriteCount() +
                ", readCount=" + getReadCount() +
                ", writeDataOffset=" + writeDataOffset +
                ", writeDataIndex=" + writeDataIndex +
                ", writeOffset=" + writeIndexOffset +
                ", writeIndex=" + writeIndex +
                ", readIndexOffset=" + readIndexOffset +
                ", readIndex=" + readIndex +
                ", readDataIndex=" + readDataIndex +
                ", readDataOffset=" + readDataOffset +
                '}';
    }
}

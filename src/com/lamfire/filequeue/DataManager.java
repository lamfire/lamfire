package com.lamfire.filequeue;


import com.lamfire.logger.Logger;
import com.lamfire.utils.Maps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

class DataManager {
    private static final Logger LOGGER = Logger.getLogger(DataManager.class);
    private MetaBuffer metaBuffer;
    private String dir;
    private String name;
    private int bufferSize = FileBuffer.DEFAULT_BUFFER_SIZE;

    private final Map<Integer,DataBuffer> stores = Maps.newHashMap();

    public DataManager(MetaBuffer metaBuffer, String dir, String name){
        this.metaBuffer = metaBuffer;
        this.dir = dir;
        this.name = name;
    }

    public void setBufferSize(int bufferSize){
        this.bufferSize = bufferSize;
    }

    public boolean expired(int index){
        return index < metaBuffer.getReadDataIndex();
    }



    private synchronized DataBuffer getOrNewDataBuffer(int index) throws IOException {
        DataBuffer io = stores.get(index);
        if (io == null) {
            File file = DataBuffer.getDataFile(dir, name, index);
            io = new DataBuffer(file, index, bufferSize);
            stores.put(index, io);
        }
        return io;
    }

    public synchronized DataBuffer getDataBuffer(int index) throws IOException {
        if(expired(index)){
            throw new ExpiredException("The index file was expired : "+ IndexBuffer.getIndexFileName(dir, name, index));
        }

        if(index > metaBuffer.getWriteDataIndex()){
            throw new FileNotFoundException("The index file out of size : "+ IndexBuffer.getIndexFileName(dir, name, index));
        }

        DataBuffer io = getOrNewDataBuffer(index);
        return io;
    }

    public void deleteDataFile(int index){

        DataBuffer io = stores.remove(index);
        if (io != null) {
            LOGGER.info("deleting index file : " + DataBuffer.getDataFileName(dir, name, index));
            io.closeAndDeleteFile();
            return;
        }

        if(DataBuffer.deleteDataFile(dir, name, index)){
            LOGGER.info("deleting index file : " + DataBuffer.getDataFileName(dir, name, index));
        }

    }

    public synchronized DataBuffer createNextDataFile()throws IOException{
        int index = metaBuffer.getWriteDataIndex() +1;
        File file = DataBuffer.getDataFile(dir, name, index);
        DataBuffer io = new DataBuffer(file, index, bufferSize);
        stores.put(index, io);
        metaBuffer.setWriteDataIndex(index);
        metaBuffer.setWriteDataOffset(0);
        return io;
    }

    public synchronized void close(){
        if(stores.isEmpty()){
            return;
        }
        for(DataBuffer io :stores.values()){
            io.close();
        }
        stores.clear();
    }
}

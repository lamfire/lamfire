package com.lamfire.filequeue;


import com.lamfire.logger.Logger;
import com.lamfire.utils.Maps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

class IndexManager {
    private static final Logger LOGGER = Logger.getLogger(IndexManager.class);
    private MetaBuffer meta;
    private String dir;
    private String name;
    private int bufferSize = FileBuffer.DEFAULT_BUFFER_SIZE;

    private final Map<Integer,IndexBuffer> indexs = Maps.newHashMap();

    public IndexManager (MetaBuffer meta,String dir,String name){
        this.meta = meta;
        this.dir = dir;
        this.name = name;
    }

    public void setBufferSize(int bufferSize){
        this.bufferSize = bufferSize;
    }

    public boolean expired(int index){
        return index < meta.getReadIndex();
    }

    public synchronized IndexBuffer getIndexBuffer(int index) throws IOException {
        if(expired(index)){
            throw new ExpiredException("The index file was expired : "+ IndexBuffer.getIndexFileName(dir, name, index));
        }

        if(index > meta.getWriteIndex()){
            throw new FileNotFoundException("The index file out of size : "+ IndexBuffer.getIndexFileName(dir, name, index));
        }

        IndexBuffer io = indexs.get(index);
        if (io == null) {
            File pageFile = IndexBuffer.getIndexFile(dir, name, index);
            io = new IndexBuffer(pageFile, index, bufferSize);
            indexs.put(index, io);
        }
        return io;
    }

    public void deleteIndexFile(int index){
        IndexBuffer io = indexs.remove(index);
        if (io != null) {
            LOGGER.info("deleting index file ["+index+"]: " + IndexBuffer.getIndexFileName(dir, name, index));
            io.closeAndDeleteFile();
            return;
        }

         if(IndexBuffer.deleteIndexFile(dir, name, index)){
             LOGGER.info("deleting index file ["+index+"]: " + IndexBuffer.getIndexFileName(dir, name, index));
         }
    }

    public synchronized IndexBuffer createNextIndexFile()throws IOException{
        meta.moveToNextWriteIndex();
        int index = meta.getWriteIndex();
        File pageFile = IndexBuffer.getIndexFile(dir, name, index);
        IndexBuffer io = new IndexBuffer(pageFile, index, bufferSize);
        indexs.put(index, io);
        return io;
    }

    public synchronized void close(){
        if(indexs.isEmpty()){
            return;
        }
        for(IndexBuffer io :indexs.values()){
            io.close();
        }
        indexs.clear();
    }
}

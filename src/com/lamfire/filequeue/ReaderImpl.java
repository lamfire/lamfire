package com.lamfire.filequeue;


import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ReaderImpl implements Reader {
    private Lock lock = new ReentrantLock();
    private MetaBuffer meta;
    private IndexManager indexMgr;
    private DataManager dataMgr;


    public ReaderImpl(MetaBuffer meta, IndexManager indexMgr, DataManager dataMgr)throws IOException{
        this.meta = meta;
        this.indexMgr = indexMgr;
        this.dataMgr = dataMgr;
    }

    @Override
    public boolean hashMore() {
        return meta.getWriteCount() > meta.getReadCount();
    }

    private byte[] read(int index,int indexOffset) throws IOException{
        try{
            lock.lock();
            IndexBuffer indexIO = indexMgr.getIndexBuffer(index);
            indexIO.setReadOffset(indexOffset);

            Element element = indexIO.take();
            DataBuffer storeIO = dataMgr.getDataBuffer(element.getStore());
            storeIO.setReadOffset(element.getPosition());
            byte[] bytes = new byte[element.getLength()];
            storeIO.read(bytes);
            return bytes;
        }finally {
            lock.unlock();
        }
    }

    public byte[] poll() throws IOException{
        if(!hashMore()){
            return null;
        }
        try{
            lock.lock();

            IndexBuffer indexIO = indexMgr.getIndexBuffer(meta.getReadIndex());
            indexIO.setReadOffset(meta.getReadIndexOffset());
            if(indexIO.getUnreadElementSize() <= 0){
                meta.moveToNextReadIndex();
                indexIO =indexMgr.getIndexBuffer(meta.getReadIndex());
                indexIO.setReadOffset(0);
            }

            Element element = indexIO.take();
            DataBuffer storeIO = dataMgr.getDataBuffer(element.getStore());
            storeIO.setReadOffset(element.getPosition());
            byte[] bytes = new byte[element.getLength()];
            storeIO.read(bytes);

            meta.setReadDataIndex(element.getStore());
            meta.setReadDataOffset(element.getPosition());
            meta.setReadIndexOffset(meta.getReadIndexOffset() + Element.ELEMENT_LENGTH);
            meta.flush();

            return bytes;
        }finally {
            lock.unlock();
        }
    }

    public byte[] peek() throws IOException{
        if(!hashMore()){
            return null;
        }
        try{
            lock.lock();
            int index = meta.getReadIndex();
            int indexOffset = meta.getReadIndexOffset();

            if((indexOffset + Element.ELEMENT_LENGTH) > FileBuffer.MAX_FILE_LENGTH){
                index++;
                indexOffset = 0;
            }
            return read(index,indexOffset);
        }finally {
            lock.unlock();
        }
    }

    public byte[] peek(int i) throws IOException{
        if(!hashMore()){
            return null;
        }
        try{
            lock.lock();
            int index = meta.getReadIndex();
            int indexOffset = meta.getReadIndexOffset();

            int skipOffset = i * Element.ELEMENT_LENGTH;
            int maxAvailableSpace = IndexBuffer.MAX_AVAILABLE_FILE_SPACE;
            int skipIdx = skipOffset / maxAvailableSpace;
            skipOffset = skipOffset % maxAvailableSpace ;
            index+= skipIdx;
            indexOffset += skipOffset;

            if(indexOffset > maxAvailableSpace) {
                index ++;
                indexOffset = indexOffset - maxAvailableSpace;
            }
            return read(index,indexOffset);
        }finally {
            lock.unlock();
        }
    }

}

package com.lamfire.filequeue;


import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class QueueReaderImpl implements QueueReader {
    private Lock lock = new ReentrantLock();
    private MetaIO meta;
    private IndexManager indexMgr;
    private StoreManager storeMgr;


    public  QueueReaderImpl(MetaIO meta,IndexManager indexMgr,StoreManager storeMgr)throws IOException{
        this.meta = meta;
        this.indexMgr = indexMgr;
        this.storeMgr = storeMgr;
    }

    @Override
    public boolean hashMore() {
        return meta.getWritedCount() > meta.getReadedCount();
    }

    public byte[] read(boolean moveToNext) throws IOException{
        if(!hashMore()){
            return null;
        }
        try{
            lock.lock();

            IndexIO indexIO = indexMgr.getIndexIO(meta.getReadIndex());
            indexIO.setReadOffset(meta.getReadIndexOffset());
            if(indexIO.getUnreadElementSize() <= 0){
                meta.setReadIndex(meta.getReadIndex() + 1);
                meta.setReadIndexOffset(0);
                indexIO =indexMgr.getIndexIO(meta.getReadIndex());
                indexIO.setReadOffset(0);
            }

            Element element = indexIO.take();
            StoreIO storeIO = storeMgr.getStoreIO(element.getStore());
            storeIO.setReadOffset(element.getPosition());
            byte[] bytes = new byte[element.getLength()];
            storeIO.read(bytes);

            if(moveToNext){
                meta.setReadStore(element.getStore());
                meta.setReadStoreOffset(element.getPosition());
                meta.setReadIndex(indexIO.getIndex());
                meta.setReadIndexOffset(meta.getReadIndexOffset() + Element.ELEMENT_LENGTH);
                meta.flush();
            }

            return bytes;
        }finally {
            lock.unlock();
        }
    }

    public byte[] peek() throws IOException{
        return read(false);
    }


    public byte[] poll() throws IOException{
        return read(true);
    }

}

package com.lamfire.filequeue;

import java.io.IOException;

/**
 * 特久化对列创建工具
 * User: lamfire
 * Date: 14-11-6
 * Time: 上午10:32
 * To change this template use File | Settings | File Templates.
 */
public class FileQueueBuilder extends Builder<FileQueue> {

    @Override
    synchronized FileQueue make(String dataDir, String name, int indexBufferSize, int storeBufferSize) throws IOException {
        FileQueueImpl fileQueue = new FileQueueImpl(dataDir,name,indexBufferSize,storeBufferSize) ;
        if(closeOnJvmShutdown()){
            fileQueue.addCloseOnJvmShutdown();
        }
        return fileQueue;
    }
}

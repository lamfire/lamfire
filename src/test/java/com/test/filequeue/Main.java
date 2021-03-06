package com.test.filequeue;

import com.lamfire.filequeue.FileQueue;
import com.lamfire.filequeue.FileQueueBuilder;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-12-24
 * Time: 上午11:13
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        FileQueueBuilder builder = new FileQueueBuilder();
        builder.dataDir("/data/FileQueue/").name("Main").closeOnJvmShutdown(true);
        FileQueue queue = builder.build();
        byte[] bytes = queue.peek();
        System.out.println(bytes);
        System.exit(0);
    }
}

package com.test.filequeue;

import com.lamfire.filequeue.FileQueue;
import com.lamfire.filequeue.FileQueueBuilder;
import com.lamfire.utils.Asserts;
import com.lamfire.utils.Bytes;

import java.util.List;


/**
 * Tests for QueueFile.
 * 
 * @author Bob Lee (bob@squareup.com)
 */
public class FileQueueTest {
	public static void main(String[] args) throws Exception {
        int buffSize = 8 * 1024 * 1024;
        FileQueueBuilder builder = new FileQueueBuilder();
        builder.dataDir("/data/FileQueue/").name("queue2").indexBlockSize(buffSize).dataBlockSize(buffSize).closeOnJvmShutdown(true);
        FileQueue queue = builder.build();
        queue.clear();
        int count = 1000000;

        //add
        for(int i=0;i<count;i++){
		    queue.push(Bytes.toBytes(i));
            if(i % 100000 == 0){
                System.out.println("[add]"+i);
            }
        }

        System.out.println("[size]:"+queue.size());

		byte[] bytes = queue.peek();
		Asserts.equalsAssert(0, Bytes.toInt(bytes));

        //peek
        for(int i=0;i<count;i++){
            bytes = queue.peek(i);
            int val = Bytes.toInt(bytes);
            Asserts.equalsAssert(i, val);
            if(i % 100000 == 0){
                System.out.println("[peek]"+i);
            }
        }
		
		//size
        Asserts.equalsAssert(count,queue.size());


        System.out.println("[size]:"+queue.size());

        //poll
        for(int i=0;i<count;i++){
            bytes = queue.pull();
            int val = Bytes.toInt(bytes);
            Asserts.equalsAssert(i, val);
            if(i % 100000 == 0){
                System.out.println("[poll]"+i);
            }
        }


        System.out.println("[size]:"+queue.size());
        Asserts.equalsAssert(0,queue.size());

        //poll(size)
        count = 10000;
        for(int i=0;i<count;i++) {
            queue.push(Bytes.toBytes(i));
        }


        int n = 0;
        while(!queue.isEmpty()) {
            List<byte[]> result = queue.pull(100);
            int i = 100 * n;
            for (byte[] b : result) {
                int v = Bytes.toInt(b);
                Asserts.equalsAssert(i++, v);
            }
            System.out.println("[size]:" + queue.size());
            Asserts.equalsAssert(count - (100 * ++n), queue.size());
        }


        System.out.println("[size]:"+queue.size());
        Asserts.equalsAssert(0,queue.size());
        System.exit(0);
	}
}

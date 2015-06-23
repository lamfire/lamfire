package com.test.filequeue;

import com.lamfire.filequeue.FileQueue;
import com.lamfire.filequeue.FileQueueBuilder;
import com.lamfire.utils.Asserts;
import com.lamfire.utils.Bytes;


/**
 * Tests for QueueFile.
 * 
 * @author Bob Lee (bob@squareup.com)
 */
public class FileQueueSkipTest {
	public static void main(String[] args) throws Exception {
        int buffSize = 8 * 1024 * 1024;
        FileQueueBuilder builder = new FileQueueBuilder();
        builder.dataDir("/data/FileQueue/").name("queue_skip").indexBufferSize(buffSize).storeBufferSize(buffSize);
        FileQueue queue = builder.build();
        queue.clear();
        int count = 1000000;

        //add
        for(int i=0;i<count;i++){
		    queue.add(Bytes.toBytes(i));
            if(i % 100000 == 0){
                System.out.println("[add]"+i);
            }
        }

        System.out.println("[size]:"+queue.size());

		byte[] bytes = queue.peek();
		Asserts.equalsAssert(0, Bytes.toInt(bytes));

        //skip
        for(int i=0;i<count;i+=4){
            bytes = queue.poll();
            queue.skip(3);
            int val = Bytes.toInt(bytes);
            Asserts.equalsAssert(i, val);
            if(i % 100000 == 0){
                System.out.println("[poll]"+i);
            }
        }
		

        queue.delete();
	}
}

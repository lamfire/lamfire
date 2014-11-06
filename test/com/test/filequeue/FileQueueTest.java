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
public class FileQueueTest {
	public static void main(String[] args) throws Exception {
        int buffSize = 8 * 1024 * 1024;
        FileQueueBuilder builder = new FileQueueBuilder();
        builder.dataDir("/data/FileQueue/").name("queue2").indexBufferSize(buffSize).storeBufferSize(buffSize);
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
		Asserts.assertEquals(0, Bytes.toInt(bytes));

        //peek
        for(int i=0;i<count;i++){
            bytes = queue.peek(i);
            Asserts.assertEquals(i, Bytes.toInt(bytes));
            if(i % 100000 == 0){
                System.out.println("[peek]"+i);
            }
        }
		
		//size
        Asserts.assertEquals(count,queue.size());


        System.out.println("[size]:"+queue.size());

        //poll
        for(int i=0;i<count;i++){
            bytes = queue.poll();
            Asserts.assertEquals(i, Bytes.toInt(bytes));
            if(i % 100000 == 0){
                System.out.println("[poll]"+i);
            }
        }


        System.out.println("[size]:"+queue.size());
        Asserts.assertEquals(0,queue.size());

        queue.delete();
	}
}

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

        queue.delete();
	}
}

package com.test.filequeue;

import com.lamfire.filequeue.FileQueue;
import com.lamfire.filequeue.FileQueueBuilder;
import com.lamfire.utils.RandomUtils;
import com.lamfire.utils.Threads;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;



/**
 * Tests for QueueFile.
 * 
 * @author Bob Lee (bob@squareup.com)
 */
public class QueueWriteTest  {
	static AtomicInteger counter = new AtomicInteger();
	
	static{
		Threads.scheduleWithFixedDelay(new Runnable() {
			int pre = 0;
			@Override
			public void run() {
				int now = counter.get();
				System.out.println((now - pre) +"/" + now);
				pre = now;
			}
		}, 1, 1, TimeUnit.SECONDS);
	}
	
	public static void main(String[] args) throws Exception {
        FileQueueBuilder builder = new FileQueueBuilder();
        builder.dataDir("/data/FileQueue/").name("queue4");
        FileQueue queue = builder.build();
		String text = RandomUtils.randomTextWithFixedLength(100);

        System.out.println(queue.size());

		while(true){
			byte[] bytes = (text +":" + (counter.get())).getBytes();
			queue.push(bytes);
			counter.getAndIncrement();
		}
		//System.out.println("[SIZE]:" + queue.size());
		//queue.close();
		//System.out.println("END");
	}
}

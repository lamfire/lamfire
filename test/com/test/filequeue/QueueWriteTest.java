package com.test.filequeue;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.lamfire.filequeue.FileQueue;
import com.lamfire.utils.RandomUtils;
import com.lamfire.utils.Threads;



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
		FileQueue queue = new FileQueue("/data/FileQueue/","test2",32 * 1024 * 1024);
		String text = RandomUtils.randomTextWithFixedLength(100);

        System.out.println(queue.size());

		while(true){
			byte[] bytes = (text +":" + (counter.get())).getBytes();
			queue.add(bytes);
			counter.getAndIncrement();
		}
		//System.out.println("[SIZE]:" + queue.size());
		//queue.close();
		//System.out.println("END");
	}
}

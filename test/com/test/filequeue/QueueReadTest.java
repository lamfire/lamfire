package com.test.filequeue;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.lamfire.filequeue.FileQueue;
import com.lamfire.utils.Threads;



/**
 * Tests for QueueFile.
 * 
 * @author Bob Lee (bob@squareup.com)
 */
public class QueueReadTest  {
	static AtomicInteger counter = new AtomicInteger();
	static byte[] bytes = null;
	static{
		Threads.scheduleWithFixedDelay(new Runnable() {
			int pre = 0;
			@Override
			public void run() {
				int now = counter.get();
				if(bytes != null){
					System.out.println((now - pre) +"   :   " + new String(bytes));
				}else{
					System.out.println((now - pre) );
				}
				pre = now;
			}
		}, 1, 1, TimeUnit.SECONDS);
	}
	
	public static void main(String[] args) throws Exception {
		FileQueue queue = new FileQueue("/data/FileQueue/","test2");

		System.out.println("[SIZE]:"+queue.size());
		
		for(int i=0;i<1000000000;i++){
			byte[] data = queue.poll();
			if(data == null){
				System.out.println("[complated]:" + counter.get());
				return;
			}else{
				bytes = data;
			}
			counter.getAndIncrement();
			//System.out.println(new String(bytes));
		}
		
		queue.close();

	}
}

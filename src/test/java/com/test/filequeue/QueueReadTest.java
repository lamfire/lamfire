package com.test.filequeue;

import com.lamfire.filequeue.FileQueue;
import com.lamfire.filequeue.FileQueueBuilder;
import com.lamfire.utils.StringUtils;
import com.lamfire.utils.Threads;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;



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
        FileQueueBuilder builder = new FileQueueBuilder();
        builder.dataDir("/data/FileQueue/").name("queue1");
		FileQueue queue = builder.build();

		System.out.println("[SIZE]:"+queue.size());
		
		for(int i=0;i<1000000000;i++){
			byte[] data = queue.pull();
			if(data == null){
				System.out.println("[complated]:" + counter.get());
				return;
			}
			bytes = data;
			String s = new String(bytes);
            String[] dd = StringUtils.split(s,':');

            int intVal = Integer.parseInt(dd[1]);
            if(intVal != counter.get()){
                System.out.println(counter.get() + " != " + intVal +" : " + s);
            }
			counter.getAndIncrement();
			//System.out.println(new String(bytes));
		}
		
		queue.close();

	}
}

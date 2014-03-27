package com.test.cache;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lamfire.simplecache.Cache;
import com.lamfire.simplecache.LRUCache;
import com.lamfire.utils.RandomUtils;

public class CacheTest {
	private static final int count = 1000;
	private static Cache<Integer, Integer> cache = new LRUCache<Integer, Integer>(count, 100000);
	private static int readCount = 0;
	private static int writeCount = 0;
	
	private static final Timer timer = new Timer("PRINTER");
	
	static Runnable writeThread = new Runnable() {
		@Override
		public void run() {
			while (true) {
				writeCount++;
				int key = RandomUtils.nextInt(count);
				cache.set(key, key);
				// System.out.println("[WRITE]:"+cache.size() +"/" +i);
			}
		}

	};

	static Runnable readThread = new Runnable() {
		@Override
		public void run() {
			while (true) {
				readCount++;
				int key = RandomUtils.nextInt(count);
				Integer item = cache.get(key);
				if (item == null) {
					System.err.println("[READ]:" + key + " -> / ----------------------------------------------  null");
				}
				System.out.println("[READ]:" + key + " -> " + item);
			}
		}
	};
	
	static void startPrinter(){
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				System.err.println("=========================write "+writeCount+"================read " + readCount);
			}
		}, 5000, 5000);
	}

	public static void main(String[] args) {

		for (int i = 0; i < count; i++) {
			cache.set(i, i);
		}
		
		startPrinter();

		ExecutorService service = Executors.newFixedThreadPool(20);
		service.submit(writeThread);

		service.submit(readThread);
		service.submit(readThread);
		service.submit(readThread);
		service.submit(readThread);
		
		
	}
}

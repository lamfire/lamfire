package com.test.cache;

import com.lamfire.simplecache.Cache;
import com.lamfire.simplecache.Caches;
import com.lamfire.utils.Printers;

public class FifoCacheTest {

	public static void main(String[] args) {
		Cache<Integer,Integer> cache = Caches.makeFifoCache(10, 10000);
		for(int i=0;i<100;i++){
			cache.set(i, i);
		}
		
		Printers.print(cache.asMap());
	}
}

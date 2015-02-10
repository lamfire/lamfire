package com.test.cache;

import com.lamfire.simplecache.Cache;
import com.lamfire.simplecache.Caches;
import com.lamfire.utils.Printers;
import com.lamfire.utils.Threads;

import java.util.Map;

public class LruCacheTest {

	public static void main(String[] args) {
		Cache<Integer,Integer> cache = Caches.makeLruCache(10, 10000);
		for(int i=0;i<100;i++){
			cache.set(i, i);
			cache.get(1);
		}
		
		Printers.print(cache.asMap());

        while(true){
            Threads.sleep(6 * 1000 );
            Printers.print("----------------------------------");
            Map<Integer,Integer> map = cache.asMap();
            Printers.print("size:" + map.size());
            Printers.print(cache.asMap());
        }
	}
}

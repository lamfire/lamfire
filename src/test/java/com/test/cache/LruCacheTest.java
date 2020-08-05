package com.test.cache;

import com.lamfire.simplecache.Cache;
import com.lamfire.simplecache.Caches;
import com.lamfire.simplecache.OnCacheRemovedListener;
import com.lamfire.utils.Printers;
import com.lamfire.utils.Threads;

import java.util.Map;

public class LruCacheTest implements OnCacheRemovedListener<Integer,Integer> {

	public static void main(String[] args) {
		Cache<Integer,Integer> cache = Caches.makeLruCache(10, 10000);
		cache.setOnCacheRemovedListener(new LruCacheTest());
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

	@Override
	public void onRemoved(Cache<Integer, Integer> cache, Integer k, Integer v) {
		System.out.println("[expired] : " + k +" -> " + v);
	}
}

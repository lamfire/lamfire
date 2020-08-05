package com.test.cache;

import com.lamfire.simplecache.Cache;
import com.lamfire.simplecache.Caches;
import com.lamfire.simplecache.OnCacheRemovedListener;
import com.lamfire.utils.Printers;

public class FifoCacheTest implements OnCacheRemovedListener<Integer,Integer> {

	public static void main(String[] args) {
		Cache<Integer,Integer> cache = Caches.makeFifoCache(10, 10000);
		cache.setOnCacheRemovedListener(new FifoCacheTest());
		for(int i=0;i<100;i++){
			cache.set(i, i);
		}
		
		Printers.print(cache.asMap());
	}

	@Override
	public void onRemoved(Cache<Integer, Integer> cache, Integer k, Integer v) {
		System.out.println("[expired] : " + k +" -> " + v);
	}
}

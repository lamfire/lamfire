package com.test.cache;

import com.lamfire.simplecache.Cache;
import com.lamfire.simplecache.Caches;
import com.lamfire.simplecache.OnCacheRemovedListener;
import com.lamfire.utils.Asserts;
import com.lamfire.utils.Printers;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class FifoCacheTest implements OnCacheRemovedListener<Integer,Integer> {

	@Test
	public  void test() {
		Cache<Integer,Integer> cache = Caches.makeFifoCache(10, 10000);
		cache.setOnCacheRemovedListener(new FifoCacheTest());
		for(int i=0;i<100;i++){
			cache.set(i, i);
		}
		Asserts.equalsAssert(10,cache.size());
		log.info("data={}",cache.asMap());
	}

	@Override
	public void onRemoved(Cache<Integer, Integer> cache, Integer k, Integer v) {
		log.info("[expired] : " + k +" -> " + v);
	}
}

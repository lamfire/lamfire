package com.test.cache;

import com.lamfire.simplecache.Cache;
import com.lamfire.simplecache.Caches;
import com.lamfire.simplecache.OnCacheRemovedListener;
import com.lamfire.utils.Asserts;
import com.lamfire.utils.Threads;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class CacheExprieTest implements OnCacheRemovedListener<String,String> {

	@Test
	public void test() {
		Cache<String,String> cache = Caches.makeLruCache(10, 10);
		cache.setOnCacheRemovedListener(new CacheExprieTest());

		for(int i=0;i<20;i++) {
			cache.set(String.valueOf(i), String.valueOf(i));
		}
		Asserts.equalsAssert(10,cache.size());

		Threads.sleep(1000);
		String val = cache.get("1");
		Asserts.nullAssert(val);

		log.info("size={}",cache.size());

	}

	@Override
	public void onRemoved(Cache<String,String> cache, String k, String v) {
		log.info("[expired] : " + k +" -> " + v);
	}

}

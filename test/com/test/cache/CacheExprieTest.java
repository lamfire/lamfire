package com.test.cache;

import com.lamfire.simplecache.Cache;
import com.lamfire.simplecache.Caches;
import com.lamfire.utils.Threads;

public class CacheExprieTest {

	public static void main(String[] args) {
		Cache<String,String> cache = Caches.makeLruCache(10, 0);
		cache.set("12345", "12345");
		Threads.sleep(1000);
		String val = cache.get("12345");
		System.out.println(val);
	}
}

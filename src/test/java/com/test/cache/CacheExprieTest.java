package com.test.cache;

import com.lamfire.simplecache.Cache;
import com.lamfire.simplecache.Caches;
import com.lamfire.simplecache.OnCacheRemovedListener;
import com.lamfire.utils.Threads;

public class CacheExprieTest implements OnCacheRemovedListener<String,String> {

	public static void main(String[] args) {
		Cache<String,String> cache = Caches.makeLruCache(10, 10);
		cache.setOnCacheRemovedListener(new CacheExprieTest());
		cache.set("12345", "12345");
		Threads.sleep(1000);
		String val = cache.get("12345");
		System.out.println(val);
	}

	@Override
	public void onRemoved(Cache<String,String> cache, String k, String v) {
		System.out.println("[expired] : " + k +" -> " + v);
	}

}

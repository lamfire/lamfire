package com.test.cache;

import com.lamfire.simplecache.Cache;
import com.lamfire.simplecache.Caches;
import com.lamfire.simplecache.OnCacheRemovedListener;
import com.lamfire.utils.Asserts;
import com.lamfire.utils.Printers;
import com.lamfire.utils.Threads;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Map;

@Slf4j
public class LruCacheTest implements OnCacheRemovedListener<Integer,Integer> {

	public  void test() {
		Cache<Integer,Integer> cache = Caches.makeLruCache(10, 10000);
		cache.setOnCacheRemovedListener(new LruCacheTest());
		for(int i=0;i<100;i++){
			cache.set(i, i);
			cache.get(1);
		}
		Asserts.equalsAssert(10,cache.size());
		Asserts.equalsAssert(1,cache.get(1).intValue());
		log.info("size={},data={}",cache.size(),cache.asMap());

		Map<Integer,Integer> map = cache.asMap();
        while(cache.asMap().size() > 0){
            Threads.sleep( 1000 );
            map = cache.asMap();
			log.info("size={},data={}",map.size(),map);
        }

		Asserts.equalsAssert(0,map.size());
	}

	@Override
	public void onRemoved(Cache<Integer, Integer> cache, Integer k, Integer v) {
		log.info("[expired] : " + k +" -> " + v);
	}
}

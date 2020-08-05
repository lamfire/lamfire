package com.lamfire.simplecache;

import java.util.Map.Entry;

public class LRUCacheMap<K, V> extends CacheMap<K, V> {
	private static final long serialVersionUID = 5464565678538L;

	public LRUCacheMap(int lruCapacity) {
		super(lruCapacity,true);
	}
}

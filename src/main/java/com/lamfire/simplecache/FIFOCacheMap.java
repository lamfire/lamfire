package com.lamfire.simplecache;

import java.util.Map.Entry;

public class FIFOCacheMap<K, V> extends CacheMap<K, V> {
	private static final long serialVersionUID = 5464565678538L;

	public FIFOCacheMap(int capacity) {
		super(capacity,false);
	}
}

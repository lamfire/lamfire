package com.lamfire.simplecache;

public interface OnCacheRemovedListener<K, V>{
	void onRemoved(Cache<K, V> cache,K k, V v);
}

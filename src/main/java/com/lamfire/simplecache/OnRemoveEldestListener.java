package com.lamfire.simplecache;

interface OnRemoveEldestListener<K, V>{
	void onRemoveEldest(K k, V v);
}

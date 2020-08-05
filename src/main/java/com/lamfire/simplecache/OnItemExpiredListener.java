package com.lamfire.simplecache;

public interface OnItemExpiredListener <K, V>{
	void onItemExpired(Item<K, V> item);
}

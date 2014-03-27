package com.lamfire.simplecache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lamfire.logger.Logger;

public class AbstractCache<K, V> implements Cache<K, V> {
	static final Logger LOGGER = Logger.getLogger(AbstractCache.class);
	protected final Lock lock = new ReentrantLock();
	private final Map<K, Item<K, V>> items;
	private int maxElementsInCache = 10000;
	private long timeToLiveMillis = 60 * 1000;

	protected AbstractCache(Map<K, Item<K, V>> items, int maxElementsInCache, long timeToLiveMillis) {
		this.maxElementsInCache = maxElementsInCache;
		this.timeToLiveMillis = timeToLiveMillis;
		this.items = items;
	}

	@Override
	public int getMaxElementsInCache() {
		return this.maxElementsInCache;
	}

	@Override
	public long getTimeToLiveMillis() {
		return this.timeToLiveMillis;
	}

	public void clear() {
		items.clear();
	}

	@Override
	public V get(K key) {
		lock.lock();
		try {
			Item<K, V> item = items.get(key);
			if (item == null) {
				return null;
			}
			if(item.isExpired(this.timeToLiveMillis)){
				this.items.remove(item.getKey());
				return null;
			}
			return item.getValue();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public boolean isEmpty() {
		return items.isEmpty();
	}

	@Override
	public boolean isExpired(Item<K, V> item) {
		return item.isExpired(this.getTimeToLiveMillis());
	}

	@Override
	public Collection<Item<K, V>> items() {
		return this.items.values();
	}

	@Override
	public Collection<K> keys() {
		return this.items.keySet();
	}

	@Override
	public Item<K, V> remove(K key) {
		lock.lock();
		try {
			return this.items.remove(key);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void set(K key, V val) {
		lock.lock();
		try {
			Item<K, V> item = items.get(key);
			if (item == null) {
				item = new Item<K, V>(key, val);
			} else {
				item.setValue(val);
			}
			items.put(key, item);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public int size() {
		return items.size();
	}

	@Override
	public Collection<V> values() {
		lock.lock();
		try {
			List<V> list = new ArrayList<V>();
			for (Item<K, V> item : items.values()) {
				list.add(item.getValue());
			}
			return list;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public Map<K, V> asMap() {
		lock.lock();
		try {
			Map<K,V> map = new LinkedHashMap<K,V>();
			for (Map.Entry<K, Item<K,V>> e: items.entrySet()) {
				map.put(e.getKey(), e.getValue().getValue());
			}
			return map;
		} finally {
			lock.unlock();
		}
	}

}

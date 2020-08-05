package com.lamfire.simplecache;

import com.lamfire.logger.Logger;
import com.lamfire.utils.Lists;
import com.lamfire.utils.Threads;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AbstractCache<K, V> implements Cache<K, V> {
	static final Logger LOGGER = Logger.getLogger("SimpleCache");
	protected final Lock lock = new ReentrantLock();
	private final Map<K, Item<K, V>> items;
	private int maxElementsInCache = 10000;
	private long timeToLiveMillis = 60 * 1000;
	private OnItemExpiredListener onItemExpiredListener;

	protected AbstractCache(Map<K, Item<K, V>> items, int maxElementsInCache, long timeToLiveMillis) {
		this.maxElementsInCache = maxElementsInCache;
		this.timeToLiveMillis = timeToLiveMillis;
		this.items = items;
        Threads.scheduleWithFixedDelay(cleanTask,5,5, TimeUnit.MINUTES);
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
				doCleanExpiredItem(item.getKey());
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

	protected synchronized void doCleanExpiredItem(K k){
		Item<K,V>  item = items.remove(k);
		if(onItemExpiredListener != null){
			onItemExpiredListener.onItemExpired(item);
		}
		if(LOGGER.isDebugEnabled()){
			LOGGER.info("clean expired key : " + k);
		}
	}

    protected synchronized void cleanExpired(){
        lock.lock();
        try {
            Set<Map.Entry<K,Item<K,V>>>  entrys = items.entrySet();
            List<K> expired = Lists.newArrayList();
            for(Map.Entry<K,Item<K,V>> e : entrys){
                Item<K,V> item = e.getValue();
                if(isExpired(item)){
                    expired.add(e.getKey());
                }
            }
            for(K k : expired){
				doCleanExpiredItem(k);
            }
        } finally {
            lock.unlock();
        }
    }

	public OnItemExpiredListener getOnItemExpiredListener() {
		return onItemExpiredListener;
	}

	public void setOnItemExpiredListener(OnItemExpiredListener onItemExpiredListener) {
		this.onItemExpiredListener = onItemExpiredListener;
	}

	private Runnable cleanTask = new Runnable() {
        @Override
        public void run() {
            cleanExpired();
        }
    };

}

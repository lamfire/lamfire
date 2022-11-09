package com.lamfire.simplecache;

import com.lamfire.logger.Logger;

import java.util.LinkedHashMap;
import java.util.Map;

public class CacheMap <K, V> extends LinkedHashMap<K, V> {
    private static final Logger LOGGER = Logger.getLogger("CacheMap");
    protected static final float LOAD_FACTOR = 1.0f;
    protected int initialCapacity = 0;
    public CacheMap(int initialCapacity,boolean accessOrder){
        super(initialCapacity,LOAD_FACTOR,accessOrder);
        this.initialCapacity = initialCapacity;
    }

    protected boolean removeEldestEntry() {
        if (size() > initialCapacity && this.entrySet().iterator().hasNext()) {
            Map.Entry<K, V> next = this.entrySet().iterator().next();
            if(next != null) {
                V v = remove(next.getKey());
                if(v != null) {
                    return true;
                }
            }
        }
        return false;
    }

}

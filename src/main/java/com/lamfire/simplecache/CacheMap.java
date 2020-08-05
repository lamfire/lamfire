package com.lamfire.simplecache;

import com.lamfire.logger.Logger;

import java.util.LinkedHashMap;
import java.util.Map;

public class CacheMap <K, V> extends LinkedHashMap<K, V> {
    private static final Logger LOGGER = Logger.getLogger("CacheMap");
    protected static final float LOAD_FACTOR = 1.0f;
    protected int initialCapacity = 0;
    protected OnRemoveEldestListener removeEldestListener;
    public CacheMap(int initialCapacity,boolean accessOrder){
        super(initialCapacity,LOAD_FACTOR,accessOrder);
        this.initialCapacity = initialCapacity;
    }

    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        if (size() > initialCapacity) {
            invokeRemoveEldestListener(eldest.getKey(),eldest.getValue());
            return true;
        }
        return false;
    }

    private void invokeRemoveEldestListener(K k ,V v){
        if(removeEldestListener != null){
            try {
                removeEldestListener.onRemoveEldest(k, v);
            }catch (Throwable t){
                LOGGER.error(t.getMessage(),t);
            }
        }
    }

    public OnRemoveEldestListener getRemoveEldestListener() {
        return removeEldestListener;
    }

    public void setRemoveEldestListener(OnRemoveEldestListener removeEldestListener) {
        this.removeEldestListener = removeEldestListener;
    }
}

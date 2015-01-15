package com.lamfire.utils;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 对象池
 * User: lamfire
 * Date: 15-1-15
 * Time: 下午1:58
 * 提供对象托管
 */
public class ObjectPool<E>{
    private int maxInstanceIdle = 1;
    private int maxInstanceSize = 8;
    private Class<E> instanceClass;

    private final AtomicInteger instanceCounter = new AtomicInteger();

    private final LinkedList<E> pool = Lists.newLinkedList();

    public ObjectPool(Class<E> instanceClass){
        this.instanceClass = instanceClass;
    }

    public int getMaxInstanceIdle() {
        return maxInstanceIdle;
    }

    public void setMaxInstanceIdle(int maxInstanceIdle) {
        this.maxInstanceIdle = maxInstanceIdle;
    }

    public int getInstanceSize(){
        return instanceCounter.get();
    }

    public int getMaxInstanceSize(){
        return maxInstanceSize;
    }

    public void setMaxInstanceSize(int maxInstanceSize){
        this.maxInstanceSize = maxInstanceSize;
    }

    public int getIdleInstanceSize(){
        return this.pool.size();
    }

    public synchronized void returnObject(E e){
        if(getIdleInstanceSize() >= maxInstanceIdle){
            discardObjectInstance(e);
            return;
        }
        pool.addLast(e);
        this.notifyAll();
    }

    public synchronized E borrowObject(){
        if(pool.isEmpty()){
            if(getInstanceSize() < getMaxInstanceSize()){
                makeObjectInstance();
            }
            waitObjectInstance();
        }
        return pool.removeFirst();
    }

    /**
     * 丢弃实例
     * @param e
     */
    private synchronized void discardObjectInstance(E e){
        instanceCounter.decrementAndGet();
    }

    /**
     * 创建实例
     */
    private synchronized void makeObjectInstance(){
        try {
            E instance = instanceClass.newInstance();
            pool.addLast(instance);
            instanceCounter.incrementAndGet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 等待实例
     */
    private synchronized void waitObjectInstance(){
        while(pool.isEmpty()){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return "ObjectPool["+instanceClass.getName()+"]{" +
                "maxInstanceIdle=" + maxInstanceIdle +
                ", maxInstanceSize=" + maxInstanceSize +
                ", instanceSize=" + instanceCounter.get() +
                ", idleInstanceSize=" +  getIdleInstanceSize() +
                '}';
    }
}

package com.lamfire.pool;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 15-2-27
 * Time: 上午10:24
 * To change this template use File | Settings | File Templates.
 */
public class FixedObjectPool<E> extends ObjectPool<E> {
    private int maxInstances = 8;

    public FixedObjectPool(PoolableObjectFactory<E> objectFactory) {
        super(objectFactory);
    }

    public int getMaxInstances(){
        return maxInstances;
    }

    public void setMaxInstances(int maxInstances){
        this.maxInstances = maxInstances;
    }


    public synchronized void returnObject(E e){
        if(getIdle() >= getMaxIdle()){
            destroy(e);
            return;
        }
        passivate(e);
        addLast(e);
        this.notifyAll();
    }

    public synchronized E borrowObject(){
        if(super.isEmpty()){
            if(getInstanceSize() < getMaxInstances()){
                make();
            }
            waitObjectInstance();
        }
        E result = super.removeFirst();
        activate(result);
        if(validate(result)){
            return result;
        }
        destroy(result);
        return borrowObject();
    }


    @Override
    public String toString() {
        return "FixedObjectPool{" +
                "maxIdle=" + getMaxIdle() +
                ", maxInstances=" + maxInstances +
                ", instance=" + getInstanceSize() +
                ", idle=" +  getIdle() +
                '}';
    }
}

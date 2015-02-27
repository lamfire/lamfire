package com.test;

import com.lamfire.pool.ObjectPool;
import com.lamfire.pool.ObjectPools;
import com.lamfire.pool.PoolableObjectFactory;
import com.lamfire.utils.RandomUtils;
import com.lamfire.utils.Threads;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 15-1-15
 * Time: 下午2:30
 * To change this template use File | Settings | File Templates.
 */
public class ObjectPoolTest implements Runnable{

    public static class Item{
        private int value = 100;
        private String name = "name";

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static class ItemFactory implements PoolableObjectFactory<Item>{

        @Override
        public Item make() {
            return new Item();
        }

        @Override
        public void destroy(Item instance) {

        }

        @Override
        public boolean validate(Item instance) {
            return true;
        }

        @Override
        public void activate(Item instance) {

        }

        @Override
        public void passivate(Item instance) {

        }
    }

    static ObjectPool<Item> pool = ObjectPools.makeCachedObjectPool(new ItemFactory(),9);

    public void run() {
        Item item = pool.borrowObject();
        Threads.sleep(RandomUtils.nextInt(1000));
        System.out.println(pool);
        pool.returnObject(item);
    }

    public static void main(String[] args) {
        ThreadPoolExecutor executor = Threads.newFixedThreadPool(20);
        while(true){
            if(executor.getTaskCount() - executor.getCompletedTaskCount() < 100){
                executor.submit(new ObjectPoolTest());
               // Threads.sleep(RandomUtils.nextInt(1000));
            }
        }
    }
}

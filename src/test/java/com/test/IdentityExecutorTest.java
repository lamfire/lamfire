package com.test;

import com.lamfire.utils.IdentityExecutor;
import com.lamfire.utils.RandomUtils;
import com.lamfire.utils.Threads;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: linfan
 * Date: 15-6-12
 * Time: 下午3:24
 * To change this template use File | Settings | File Templates.
 */
public class IdentityExecutorTest implements Runnable{
    static AtomicInteger counter = new AtomicInteger();

    private int id;

    public  IdentityExecutorTest(int id){
        this.id = id;
    }

    @Override
    public void run() {
        Threads.sleep(10);
        System.out.println("[RUN]:"+counter.incrementAndGet() + " -> " + id);
    }


    public static void main(String[] args) {
        IdentityExecutor executor = new IdentityExecutor(Threads.newFixedThreadPool(1));
        for(int i=0;i< 1000 ;i++){
            try{
                int id = RandomUtils.nextInt(10);
                executor.submit(id,new IdentityExecutorTest(id));
            }catch (Throwable t){
                System.out.println(t.getMessage());
            }
        }
        System.out.println("==================================================submit finish");
        Threads.sleep(1000);
        System.out.println("[Waiting]:"+executor.getWaitingTaskIds().size());
        executor.shutdown();
    }
}

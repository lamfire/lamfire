package com.lamfire.utils;

import com.lamfire.logger.Logger;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *操作频率统计器
 */
public class OPSMonitor{
    private static final Logger LOGGER = Logger.getLogger(OPSMonitor.class);

    private final AtomicInteger counter = new AtomicInteger(0);
    private ScheduledThreadPoolExecutor executor;
    private String id;
    private int ops = 0;
    private int prevCount = 0;
    private int interval = 1;
    private boolean debug = false;
    private long prevTime = System.nanoTime();
    private long lastExpendTime = 0;

    private long maxExpendTime = 0;
    private long totalExpendTime = 0;

    private Runnable worker = new Runnable() {
        @Override
        public void run() {
            int thisCount = counter.get();
            ops = Math.abs(thisCount - prevCount);
            prevCount = thisCount;
            if (debug) {
                LOGGER.info(OPSMonitor.this.toString());
            }
        }
    } ;

    public OPSMonitor(String id){
        this.id = id;
    }



    public synchronized void done(){
        long thisTime = System.nanoTime();
        counter.incrementAndGet();
        this.lastExpendTime = thisTime - prevTime;
        if(this.maxExpendTime < lastExpendTime){
            this.maxExpendTime = lastExpendTime;
        }
        this.totalExpendTime += lastExpendTime;
        this.prevTime = thisTime;
    }

    public long getLastExpendTimeNano(){
        return this.lastExpendTime;
    }

    public long getLastExpendTimeMillis(){
        return this.lastExpendTime /1000/1000;
    }

    public long getLastExpendTimeSecond(){
        return this.lastExpendTime /1000 /1000 /1000;
    }

    public long getMaxExpendTimeNano(){
        return  this.maxExpendTime;
    }

    public long getMaxExpendTimeMillis(){
        return  this.maxExpendTime /1000/1000;
    }

    public long getMaxExpendTimeSecond(){
        return  this.maxExpendTime /1000/1000/1000;
    }

    public long getAvgExpendTimeNano(){
        int count = counter.get();
        if(count ==0){
            return 0;
        }
        return  this.totalExpendTime / count;
    }

    public long getAvgExpendTimeMillis(){
        return  getAvgExpendTimeNano() /1000/1000;
    }

    public long getAvgExpendTimeSecond(){
        return  getAvgExpendTimeNano() /1000/1000/1000;
    }

    public long getTotalExpendTimeNano(){
        return  this.totalExpendTime;
    }

    public long getTotalExpendTimeMillis(){
        return  this.totalExpendTime /1000/1000;
    }

    public long getTotalExpendTimeSecond(){
        return  this.totalExpendTime/1000/1000/1000;
    }

    public int getOps(){
        return ops;
    }

    public int getCount(){
        return this.counter.get();
    }

    public String getId(){
        return id;
    }

    public void setInterval(int intervalSecond){
        this.interval = intervalSecond;
    }

    public int getInterval(){
        return this.interval;
    }

    public void startup(){
        executor = Threads.newSingleThreadScheduledExecutor();
        executor.scheduleWithFixedDelay(worker, interval, interval, TimeUnit.SECONDS);
    }

    public void debug(boolean debug) {
        this.debug = debug;
    }

    public void shutdown(){
        executor.shutdown();
    }

    public String toString(){
        return "[" + this.id + "]count=" + counter.get() + ",ops=" + this.ops + "/" + OPSMonitor.this.interval + "s";
    }
}

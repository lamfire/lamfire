package com.lamfire.utils;


import com.lamfire.logger.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

public class PerformanceTracker {
    private static final Logger LOGGER = Logger.getLogger(PerformanceTracker.class);
    private Class<? extends Runnable> taskClass;
    private ThreadPoolExecutor workThreadPool;
    private ExecutorService producerThread = Threads.newFixedThreadPool(1);
    private int maxIdle = 128;
    private boolean running = true;
    private final OPSMonitor monitor = new OPSMonitor("PerformanceTracker");

    public PerformanceTracker(ThreadPoolExecutor workThreadPool, Class<? extends Runnable> taskClass) {
        this.taskClass = taskClass;
        this.workThreadPool = workThreadPool;
    }

    private class TaskProxy implements Runnable {
        Runnable real;

        public TaskProxy(Runnable real) {
            this.real = real;
        }

        public void run() {
            real.run();
            monitor.done();
        }
    }

    private final Runnable producer = new Runnable() {
        @Override
        public void run() {
            while (running) {
                if (workThreadPool.getActiveCount() > maxIdle) {
                    Threads.sleep(100);
                }
                try {
                    workThreadPool.submit(new TaskProxy(taskClass.newInstance()));
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    };

    public void startup() {
        producerThread.submit(producer);
        monitor.startup();
    }

    public void shutdown() {
        running = false;
        monitor.shutdown();
        producerThread.shutdown();
        workThreadPool.shutdown();
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public OPSMonitor getMonitor() {
        return monitor;
    }

    public void setMonitorI(int second) {
        this.monitor.setInterval(second);
    }
}

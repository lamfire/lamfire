package com.test;

import com.lamfire.utils.PerformanceTracker;
import com.lamfire.utils.Threads;

/**
 * Created by lamfire on 16/11/30.
 */
public class PerformanceTrackerTest implements Runnable {

    public static void main(String[] args) {
        PerformanceTracker tracker = new PerformanceTracker(Threads.newCachedThreadPool(), PerformanceTrackerTest.class);
        tracker.startup();

        Threads.sleep(3000);
        tracker.shutdown();
    }

    @Override
    public void run() {
        //System.out.println("work!!!");
    }
}

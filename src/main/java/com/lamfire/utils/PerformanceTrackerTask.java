package com.lamfire.utils;


class PerformanceTrackerTask implements Runnable {
    Runnable real;
    OPSMonitor monitor;

    public PerformanceTrackerTask(OPSMonitor monitor, Runnable real) {
        this.real = real;
        this.monitor = monitor;
    }

    public void run() {
        real.run();
        monitor.done();
    }
}

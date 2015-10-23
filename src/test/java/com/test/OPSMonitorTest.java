package com.test;

import com.lamfire.utils.OPSMonitor;

/**
 * Created with IntelliJ IDEA.
 * User: linfan
 * Date: 15-10-23
 * Time: 下午4:55
 * To change this template use File | Settings | File Templates.
 */
public class OPSMonitorTest {

    public static void main(String[] args) {
        OPSMonitor monitor = new OPSMonitor("11");
        monitor.startup();

        while(true){
            monitor.done();
        }
    }
}

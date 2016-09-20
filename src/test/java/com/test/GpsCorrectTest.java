package com.test;

import com.lamfire.utils.GpsCorrect;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 15-1-7
 * Time: 上午11:37
 * To change this template use File | Settings | File Templates.
 */
public class GpsCorrectTest {
    public static void main(String[] args) {
        double lat =29.52282;   //106.52400,29.52282
        double lon =106.52400;
        double[] latlon = GpsCorrect.transform(lat, lon);
        System.out.println(latlon[0] +"," + latlon[1]);
    }
}

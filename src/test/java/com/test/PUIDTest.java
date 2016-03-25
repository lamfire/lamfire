package com.test;

import com.lamfire.code.PUID;

/**
 * Created by lamfire on 16/3/25.
 */
public class PUIDTest {
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println(PUID.makeAsString());
        }

    }
}

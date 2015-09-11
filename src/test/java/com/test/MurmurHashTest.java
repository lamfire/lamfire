package com.test;

import com.lamfire.code.MurmurHash;

import java.nio.ByteBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: linfan
 * Date: 15-9-11
 * Time: 上午11:09
 * To change this template use File | Settings | File Templates.
 */
public class MurmurHashTest {
    public static void main(String[] args) {
        byte[] bytes = "123456".getBytes();
        System.out.println(MurmurHash.hash32(bytes,0,bytes.length,-1));       // -2144263435
        System.out.println(MurmurHash.hash64(bytes,0,bytes.length,-1));       //-1077195886890043841
    }
}

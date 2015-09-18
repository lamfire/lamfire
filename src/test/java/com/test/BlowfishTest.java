package com.test;

import com.lamfire.code.AlgorithmCoder;
import com.lamfire.code.Blowfish;
import com.lamfire.code.Hex;

/**
 * Created with IntelliJ IDEA.
 * User: linfan
 * Date: 15-9-18
 * Time: 下午3:41
 * To change this template use File | Settings | File Templates.
 */
public class BlowfishTest {
    static String KEY = "abcd1234";
    static String DATA = "好好学习,开开向上!";

    public static void jdk() throws Exception {
        AlgorithmCoder coder = new AlgorithmCoder(AlgorithmCoder.Blowfish.AlgorithmName, AlgorithmCoder.Blowfish.Transformation_CBC_PKCS5Padding,AlgorithmCoder.Blowfish.InitializationVector);

        byte[] key = (KEY.getBytes());

        byte[] e = coder.encode(DATA.getBytes(),key);

        System.out.println("[ENCODE] : "+Hex.encode(e));

        byte[] d = coder.decode(e,key);

        System.out.println("[DECODE] : "+new String(d));
    }

    public static void main(String args[]) throws Exception {
        jdk();


        Blowfish coder = new Blowfish(KEY.getBytes());
        byte[] enBytes =  coder.encode(DATA.getBytes());
        System.out.println("[ENCODE] : " + Hex.encode(enBytes));

        byte[] bytes = coder.decode(enBytes);
        System.out.println("[DECODE] : "+new String(bytes));
    }
}

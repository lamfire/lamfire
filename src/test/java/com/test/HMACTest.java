package com.test;

import com.lamfire.code.*;

/**
 * Created by lamfire on 16/3/24.
 */
public class HMACTest {
    public static void main(String[] args)throws Exception {
        byte[] source = "1234567890".getBytes();

        HMAC hmac = new HMAC(MD5.getInstance());
        System.out.println("[HMAC_MD5]     : "+Hex.encode(hmac.digest(source,source)));
        System.out.println("[HMAC_MD5_JDK] : "+Hex.encode(HmacMD5.digest(source,source)));

        hmac = new HMAC(SHA1.getInstance());
        System.out.println("[HMAC_SHA1]     : "+Hex.encode(hmac.digest(source,source)));
        System.out.println("[HMAC_SHA1_JDK] : "+Hex.encode(HmacSHA1.digest(source,source)));

        hmac = new HMAC(SHA256.getInstance());
        System.out.println("[HMAC_SHA256]     : "+Hex.encode(hmac.digest(source,source)));

        hmac = new HMAC(SHA512.getInstance());
        System.out.println("[HMAC_SHA512]     : "+Hex.encode(hmac.digest(source,source)));
    }
}

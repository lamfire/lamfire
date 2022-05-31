package com.lamfire.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {
    private static final Logger log = LoggerFactory.getLogger(AESUtils.class);
    public static final String AES_ALGORITHM = "AES";

    private AESUtils() {
    }

    public static byte[] encrypt(byte[] data, byte[] key) {
        if (key.length < 16 || key.length > 16) {
            throw new RuntimeException("Invalid AES key length (must be 16 bytes)");
        }
        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(1, genKey(key));
            byte[] result = cipher.doFinal(data);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("加密数据失败:"+e.getMessage(),e);
        }
    }

    public static byte[] decrypt(byte[] data, byte[] key) {
        if (key.length < 16 || key.length > 16) {
            throw new RuntimeException("Invalid AES key length (must be 16 bytes)");
        }

        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(2, genKey(key));
            byte[] result = cipher.doFinal(data);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("解密数据失败:"+e.getMessage(),e);
        }

    }

    private static SecretKeySpec genKey(byte[] key) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key, AES_ALGORITHM);
            byte[] enCodeFormat = secretKey.getEncoded();
            return new SecretKeySpec(enCodeFormat, AES_ALGORITHM);
        } catch (Exception e) {
            throw new RuntimeException("gen aes key fail!", e);
        }
    }

}

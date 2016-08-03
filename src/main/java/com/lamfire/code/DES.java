package com.lamfire.code;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

public class DES {

    public static final String ALGORITHM = "DES";
    public static final String PADDING = "DES/CBC/PKCS5Padding";
    private static final byte[] DEFAULT_IV = ("00000000".getBytes()) ;

    private static Key getKey(byte[] key) throws Exception {
        DESKeySpec dks = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        SecretKey secretKey = keyFactory.generateSecret(dks);
        return secretKey;
    }

    public static byte[] decode(byte[] data, byte[] key) throws Exception {
        return decode(data,key,DEFAULT_IV);
    }


    public static byte[] encode(byte[] data, byte[] key) throws Exception {
        return encode(data,key,DEFAULT_IV);
    }

    public static byte[] decode(byte[] data, byte[] key, byte[] ivBytes) throws Exception {
        Key secretKey = getKey(key);
        Cipher cipher = Cipher.getInstance(PADDING);
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
        return cipher.doFinal(data);

    }

    public static byte[] encode(byte[] data, byte[] key, byte[] ivBytes) throws Exception {
        Key secretKey = getKey(key);
        Cipher cipher = Cipher.getInstance(PADDING);
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
        byte[] bytes = cipher.doFinal(data);
        return bytes;
    }


    public static void main(String[] args) throws Exception {
        final String inputStr = "好好学习,开开向上";
        final String key = "lin12345111";
        System.err.println("原文:\t" + inputStr);
        System.err.println("密钥:\t" + key);

        byte[] inputData;
        byte[] outputData;
        String outputStr;

        //not iv
        inputData = DES.encode(inputStr.getBytes(), key.getBytes());
        System.out.println("加密后:\t" + Base64.encode(inputData));
        outputData = DES.decode(inputData, key.getBytes());
        outputStr = new String(outputData);
        System.out.println("解密后:\t" + outputStr);
        System.out.println(inputStr.equals(outputStr));


        //use iv
        byte[] ivBytes = "12345679".getBytes();
        inputData = DES.encode(inputStr.getBytes(), key.getBytes(), ivBytes);
        System.out.println("加密后[IV]:\t" + Base64.encode(inputData));
        outputData = DES.decode(inputData, key.getBytes(), ivBytes);
        outputStr = new String(outputData);
        System.out.println("解密后[IV]:\t" + outputStr);
        System.out.println(inputStr.equals(outputStr));





    }
}

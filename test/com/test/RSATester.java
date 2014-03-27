package com.test;

import java.security.KeyPair;

import com.lamfire.code.RSA;

public class RSATester {
	static String source = "RSA公钥加密算法是1977年由Ron Rivest、Adi Shamirh和LenAdleman在（美国麻省理工学院）开发的。RSA取名来自开发他们三者的名字。RSA是目前最有影响力的公钥加密算法，它能够抵抗到目前为止已知的所有密码攻击，已被ISO推荐为公钥数据加密标准。RSA算法基于一个十分简单的数论事实：将两个大素数相乘十分容易，但那时想要对其乘积进行因式分解却极其困难，因此可以将乘积公开作为加密密钥。";
	static String publicKey;
    static String privateKey;

    static {
        try {
        	KeyPair keyPair = RSA.genKeyPair();
            publicKey = RSA.getPublicKey(keyPair);
            privateKey = RSA.getPrivateKey(keyPair);
           

            System.err.println("公钥: \n" + publicKey);
            System.err.println("\n私钥： \n" + privateKey);
            System.err.println("\nModulus： \n" + RSA.getModulus(keyPair));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws Exception {
        test();
        //testSign();
    }

    static void test() throws Exception {
        System.err.println("\n\n================================公钥加密——私钥解密==============================");
        System.out.println("\r加密前文字：\r\n" + source);
        byte[] data = source.getBytes();
        byte[] encodedData = RSA.encodeByPublicKey(data, publicKey);
        System.err.println("加密后文字：\r\n" + new String(encodedData));
        byte[] decodedData = RSA.decodeByPrivateKey(encodedData, privateKey);
        String target = new String(decodedData);
        System.out.println("解密后文字: \r\n" + target);
        
        System.err.println("\n\n=======================私钥加密——公钥解密=======================");
        System.out.println("原文字：\r\n" + source);
        data = source.getBytes();
        encodedData = RSA.encodeByPrivateKey(data, privateKey);
        System.err.println("加密后：\r\n" + new String(encodedData));
        decodedData = RSA.decodeByPublicKey(encodedData, publicKey);
        target = new String(decodedData);
        System.out.println("解密后: \r\n" + target);
    }

    static void testSign() throws Exception {
        System.err.println("\n\n=======================私钥签名——公钥验证签名=======================");
        String sign = RSA.signature(source.getBytes(), privateKey);
        System.err.println("签名:\r" + sign);
        boolean status = RSA.verifySignature(source.getBytes(),sign, publicKey);
        System.out.println("\n\n验证结果:" + status);
    }
}

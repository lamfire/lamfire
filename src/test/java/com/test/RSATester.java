package com.test;

import java.security.KeyPair;

import com.lamfire.code.Base64;
import com.lamfire.code.RSA;

public class RSATester {
	static String source = "RSA公钥加密算法是1977年由Ron Rivest、Adi Shamirh和LenAdleman在（美国麻省理工学院）开发的。RSA取名来自开发他们三者的名字。RSA是目前最有影响力的公钥加密算法，它能够抵抗到目前为止已知的所有密码攻击，已被ISO推荐为公钥数据加密标准。RSA算法基于一个十分简单的数论事实：将两个大素数相乘十分容易，但那时想要对其乘积进行因式分解却极其困难，因此可以将乘积公开作为加密密钥。";
	static String privateKey ="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIao0vPHmHmQGOT9ub/xf1fp6KNT\n" +
            "lMTrSgS+ucN0sJWy1BUODzDkYVgtFy/wh2tYHWyxdIt10LdzfUipse2lkpOYJGQfd2c9YaKTkHsT\n" +
            "o6vbCmMe6KOqCW/My0W8t1ZirAbD7a22m8w7i2GPKCCe+gtwArWNSODSoTgvXLxGcynRAgMBAAEC\n" +
            "gYBEzljUBaXMX6vIvji+chh+hF/BrMB0ikFNAWOMEsD0DOc/RurOMbPnyUpyzXV0jRaj5lzFsdKP\n" +
            "GmzmxwbKhnLJR52z7IYnZ3efHTPLFrODGG4tA7s+nQieHv8YhJYUP9bGz1vKbTAV0gl6qwpyMVKr\n" +
            "4/sVVdgQIHMXV45eSZi/DQJBAMcJDvkd2+NXPBt6mcMUL74qaAbsFpIGWWPeWKvZWSPJsPvyIpFt\n" +
            "yHgNia8DD05ovCfLb1nVpyDJmoYyDvP6ElsCQQCtMxDCELSk2h6CpMIolNpdjh4L1kdjA1S7nf9k\n" +
            "jrEBTi6vUtsf9ky1f9c+pf6j7VXtlHI0av4AIWuBmYMN99RDAkAzTD9ir3Jr3qDsSCX3JG3FqnJT\n" +
            "TmfmzOIfHkBmUkdqNOJ7nQWRy+S+JGcXB0fbopzT+NfDJJDFn5BHBAHcmdg/AkB8Z+unmZxHtSyp\n" +
            "JnMX9yOKg6RtWDLame/o+6oQrrXRfDjoUhl3MdETpYoUQ+NOGBHanwARSrC7o0W3R27tP5BFAkAa\n" +
            "RzPVJYA30Ggkx2Pwxuuu+I92XviCMuu8bh3HOKUZxrQglVI0zAGq+H4qbjrYruF5dG0K1SWEWNsf\n" +
            "JPMw3Zd+";

    static String publicKey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCGqNLzx5h5kBjk/bm/8X9X6eijU5TE60oEvrnD\n" +
            "dLCVstQVDg8w5GFYLRcv8IdrWB1ssXSLddC3c31IqbHtpZKTmCRkH3dnPWGik5B7E6Or2wpjHuij\n" +
            "qglvzMtFvLdWYqwGw+2ttpvMO4thjyggnvoLcAK1jUjg0qE4L1y8RnMp0QIDAQAB";


    static int keySize = 512;

    static void genKey(){
        try {
        	KeyPair keyPair = RSA.genKeyPair(keySize);
            byte[] pubKey = RSA.getPublicKey(keyPair);
            byte[] prvKey = RSA.getPrivateKey(keyPair);
            byte[] modulus =  RSA.getModulus(keyPair);

            publicKey = Base64.encode(pubKey);
            privateKey = Base64.encode(prvKey) ;

            System.err.println("公钥: \n" +(publicKey));
            System.err.println("\n私钥： \n" + (privateKey));
            System.err.println("\nModulus： \n" + Base64.encode(modulus));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    


    static void test() throws Exception {
        System.out.println("\n\n================================公钥加密——私钥解密==============================");
        System.out.println("加密前文字：\n" + source);
        byte[] data = source.getBytes();

        RSA rsa = new RSA(keySize,Base64.decode(privateKey),Base64.decode(publicKey));

        byte[] encodedData = rsa.encodeByPublicKey(data);
        System.err.println("加密后文字：\n" + new String(encodedData));
        byte[] decodedData = rsa.decodeByPrivateKey(encodedData);
        String target = new String(decodedData);
        System.out.println("解密后文字: \n" + target);
        
        System.err.println("\n\n=======================私钥加密——公钥解密=======================");
        System.out.println("原文字：\n" + source);
        data = source.getBytes();
        encodedData = rsa.encodeByPrivateKey(data);
        System.err.println("加密后：\n" + new String(encodedData));
        decodedData = rsa.decodeByPublicKey(encodedData);
        target = new String(decodedData);
        System.out.println("解密后: \n" + target);
    }

    static void testSign() throws Exception {
        System.err.println("\n\n=======================私钥签名——公钥验证签名=======================");
        RSA rsa = new RSA(keySize,Base64.decode(privateKey),Base64.decode(publicKey));

        String sign = rsa.signatureAsBase64(source.getBytes());
        System.err.println("签名:" + sign);
        boolean status = rsa.verifySignatureAsBase64(source.getBytes(),sign);
        System.out.println("\n\n验证结果:" + status);
    }

    public static void main(String[] args) throws Exception {
        keySize = 512;
        genKey();
        test();
        //testSign();
    }
}

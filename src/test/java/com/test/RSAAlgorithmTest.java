package com.test;

import com.lamfire.code.Base64;
import com.lamfire.code.Hex;
import com.lamfire.code.RSAAlgorithm;
import com.lamfire.utils.*;

import java.math.BigInteger;

/**
 * Created with IntelliJ IDEA.
 * User: linfan
 * Date: 15-6-11
 * Time: 下午8:56
 * To change this template use File | Settings | File Templates.
 */
public class RSAAlgorithmTest {
    static BigInteger privateKey = new BigInteger("18d260e2fd33a08ae4a638952befb33496f072e00ce92029f84a1cf0e3ef0c9ef50a60f84cf91c7a0d69cf8d2feaaeb06aae75e94273ecf1684340da1d43b00707ce1a5b4e0063bb6aa9a6c63b9ab7aab59b716e2d514d648caf10be5231394a5d93434589ff9538359fd6e5055341ce0f9d032ffe20db1b3e1b940842b4e49f",16);
    static BigInteger publicKey = new BigInteger("d4d2c80d52ddb2ed4394bb3f8e6c6b82d55f0e8316da658f5262d4b1aa32bd63676604cba4a4f4bf8e511a5c6d680a12d9b9ce30d412516dcfa5165d50617547969536a46dd120502447ffa10c2344d64b900033908eb1b254625b459b6eee8468fde9bf35eaa2a89881823672d70659cdf990ee264823db6a1bea10412242d7",16);
    static BigInteger modulus = new BigInteger("1e72a06a87163b2fb694f3caaf225c6cfa102ebf2b188088997ce7ae958617c1cafbcf445c201f87b82b913d581cf18681673b2a02f2b164de1e1b9569f56bd61f611068f252db8a3cf56ef98ed91e4f7cde10eb364b12d1229f3d4615594945e444a13a0f10362e7b6cb7cf5d2d0ed0d87ca2ec37e1545e8b674427badd6d67",16);
    static final int keyBits = 1024;

    static void endetest(byte[] bytes , RSAAlgorithm rsa,BigInteger privateKey,BigInteger publicKey,BigInteger modulus){
        byte[] cipher =rsa.encode(bytes,privateKey,modulus);
        // 解密
        byte[] plain = rsa.decode(cipher,publicKey,modulus);
        if(!Bytes.equals(bytes,plain)){
            System.out.println("[SOURCE]:" + Hex.encode(bytes));
            System.out.println("[DECODE]:" + Hex.encode(plain));
        }
        Asserts.equalsArrayAssert(bytes,plain);
        System.out.println(new String(plain));
    }

    private static void testFile() throws Exception {
        RSAAlgorithm rsa = new RSAAlgorithm(keyBits);


        String source = IOUtils.toString(ClassLoaderUtils.getResourceAsStream("data.txt", RSAAlgorithm.class));
        System.out.println("source:");
        System.out.println(source);
        System.out.println("---------------------------------------------------");
        byte[] bytes = rsa.encode(source.getBytes("utf-8"),publicKey,modulus);
        byte[] deBytes = rsa.decode(bytes,privateKey,modulus);
        String de = new String(deBytes,"utf-8");

        System.out.println("decode:");
        System.out.println(de);

        Asserts.equalsAssert(source,de);
    }

    private static void testKey() throws Exception {
        // 加密
        String source = "RSA公钥加密算法是1977年由Ron Rivest、Adi Shamirh和LenAdleman在（美国麻省理工学院）开发的。RSA公钥加密算法是1977年由Ron Rivest、Adi Shamirh和LenAdleman在（美国麻省理工学院）开发的。";
        System.out.println("privKey:" + privateKey.bitLength());
        System.out.println(privateKey);

        System.out.println("pubKey:" + publicKey.bitLength());
        System.out.println(publicKey);

        System.out.println("modulus:" + modulus.bitLength());
        System.out.println(modulus);

        System.out.println("============================");

        RSAAlgorithm rsa = new RSAAlgorithm(keyBits);


        byte[] bytes = source.getBytes("utf-8");
        System.out.println("[SOURCE BYTES]:" + bytes.length);
        byte[] cipher =rsa.encode(bytes,privateKey,modulus);
        System.out.println("[ENCODE BYTES]:" + cipher.length);
        System.out.println("[ENCODE]:\r\n" + Hex.encode(cipher));


        System.out.println("============================");
        // 解密
        byte[] plain = rsa.decode(cipher,publicKey,modulus);
        String decode =  new String(plain, "UTF-8");
        System.out.println("[DECODE BYTES]:" + plain.length);
        System.out.println("[DECODE]:\r\n" +decode);

        //Asserts.assertEquals(source,decode);
    }

    private static void test() throws Exception {
//        BigInteger Q = new BigInteger(Hex.decode("8682236E835BAF3CFEC279F0C479558EF55EADA5836D1D1B18D6D85736EB71F263D440DC9E7E0A997BECDD05B60EC19D63AC71EC27D91269739453D5050A6783"));
//        BigInteger P =  new BigInteger(Hex.decode("F4FE37F84135BFB46DCBF5F58976990E0922969FA129A1B210B9036F2B9236399BABF2580582B5EEFD49EA6151A938CABD3405B70483E7DFB4183BDE41A3FD1B"));
//        BigInteger E = new BigInteger(Hex.decode("B60432D2F12CD321D5F827E67DE1B5BCA51A13BEB59B9969E6A7E509F0442FF6FC9AE091E8B4B49B1A8AC275FF9A1922F4EDED500AE1E08ED64D2974BDA352A3"));

        BigInteger Q = RSAAlgorithm.genProbablePrime(511);
        BigInteger P =  RSAAlgorithm.genProbablePrime(511);
        BigInteger E =  RSAAlgorithm.genProbablePrime(1024);
        System.out.println(String.format("Q:%d , P:%d , E:%d", Q.bitLength(), P.bitLength(), E.bitLength()));


        RSAAlgorithm rsa = new RSAAlgorithm(1024,P,Q,E);

        System.out.println("PrivateKey[" + rsa.getPrivateKey().bitLength() +"]:" + rsa.getPrivateKey().toString(16));
        System.out.println("PublicKey[" + rsa.getPublicKey().bitLength() +"]:" +rsa.getPublicKey().toString(16));
        System.out.println("Modulus[" + rsa.getModulus().bitLength()+"]:"+rsa.getModulus().toString(16));

        System.out.println("============================");


        // 加密
        String source = "RSA公钥加密算法是1977年由Ron Rivest、Adi Shamirh和LenAdleman在（美国麻省理工学院）开发的。";
        byte[] bytes = source.getBytes("utf-8");

        System.out.println("[SOURCE BYTES]:" + bytes.length);
        byte[] cipher =rsa.encode(bytes,rsa.getPrivateKey(),rsa.getModulus());
        System.out.println("[ENCODE BYTES]:" + cipher.length);
        System.out.println("[ENCODE]:\r\n" + Hex.encode(cipher));


        System.out.println("============================");
        // 解密
        byte[] plain = rsa.decode(cipher,rsa.getPublicKey(),rsa.getModulus());
        String decode =  new String(plain, "UTF-8");
        System.out.println("[DECODE BYTES]:" + plain.length);
        System.out.println("[DECODE]:\r\n" +decode);

        //Asserts.assertEquals(source,decode);
    }

    public static void randomTest() throws Exception {
        RSAAlgorithm rsa = new RSAAlgorithm(keyBits);
        //rsa.genKey();

        String source = RandomUtils.randomTextWithFixedLength(117);
        System.out.println(source);
        byte[] bytes = source.getBytes();

        byte[] enBytes = rsa.encode(bytes,privateKey,modulus);

        byte[] deBytes = rsa.decode(enBytes,publicKey,modulus);

        String des = new String(deBytes);
        System.out.println(des);
        Asserts.equalsAssert(source,des);
    }

    public static void testGenKey()throws Exception{
        BigInteger Q = RSAAlgorithm.genProbablePrime(511);
        BigInteger P =  RSAAlgorithm.genProbablePrime(511);
        BigInteger E =  RSAAlgorithm.genProbablePrime(1024);
        System.out.println(String.format("Q:%d , P:%d , E:%d", Q.bitLength(), P.bitLength(), E.bitLength()));


        RSAAlgorithm rsa = new RSAAlgorithm(1024,P,Q,E);

        privateKey = rsa.getPrivateKey();
        publicKey = rsa.getPublicKey();
        modulus = rsa.getModulus();


        System.out.println("PrivateKey[" + rsa.getPrivateKey().bitLength() +"]:" + rsa.getPrivateKey().toString(16));
        System.out.println("PublicKey[" + rsa.getPublicKey().bitLength() +"]:" +rsa.getPublicKey().toString(16));
        System.out.println("Modulus[" + rsa.getModulus().bitLength()+"]:"+rsa.getModulus().toString(16));
    }

    public static void test(String source) throws Exception {
        RSAAlgorithm rsa = new RSAAlgorithm(keyBits);
        System.out.println(source);
        byte[] bytes = source.getBytes();

        //rsa.genKey();

        byte[] enBytes = rsa.encode(bytes,privateKey,modulus);

        byte[] deBytes = rsa.decode(enBytes,publicKey,modulus);

        String des = new String(deBytes);
        System.out.println(des);
        Asserts.equalsAssert(source,des);
    }

    public static void main(String[] args) throws Exception {
        testFile();
        //testGenKey();
        //test("姓名");

    }
}

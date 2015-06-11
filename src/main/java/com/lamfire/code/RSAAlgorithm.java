package com.lamfire.code;

import com.lamfire.utils.Asserts;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

/**
 * RSA算法
 * 
 * @author lamfire
 * 
 */
public class RSAAlgorithm {
	static final Random random = new SecureRandom();
    private int keyBitLength = 1024;
	private BigInteger publicKey;
	private BigInteger privateKey;
	private BigInteger modulus;
    private int maxEncryptBlock;
    private int maxDecryptBlock;

	public RSAAlgorithm(int keyBitLength) {
        this.keyBitLength = keyBitLength;
        this.maxEncryptBlock = keyBitLength / 8 - 11;
        this.maxDecryptBlock = keyBitLength / 8;
		genKey(keyBitLength);
	}

	public RSAAlgorithm(BigInteger p, BigInteger q, BigInteger e) {
		genKey(p, q, e);
	}

	public RSAAlgorithm(String p, String q, String e) {
		BigInteger P = new BigInteger(Base64.decode(p));
		BigInteger Q = new BigInteger(Base64.decode(q));
		BigInteger E = new BigInteger(Base64.decode(e));
		genKey(P, Q, E);
	}

	/**
	 * 获取私钥
	 * 
	 * @return
	 */
	public BigInteger getPrivateKey() {
		return this.privateKey;
	}

	/**
	 * 获取公钥
	 * 
	 * @return
	 */
	public BigInteger getPublicKey() {
		return this.publicKey;
	}

	/**
	 * 获取Modulus
	 * 
	 * @return
	 */
	public BigInteger getModulus() {
		return this.modulus;
	}

	/**
	 * 获取私钥
	 * 
	 * @return
	 */
	public String getPrivateKeyAsBase64() {
		return Base64.encode(this.privateKey.toByteArray());
	}

	/**
	 * 获取公钥
	 * 
	 * @return
	 */
	public String getPublicKeyAsBase64() {
		return Base64.encode(this.publicKey.toByteArray());
	}

	/**
	 * 获取Modulus
	 * 
	 * @return
	 */
	public String getModulusAsBase64() {
		return Base64.encode(this.modulus.toByteArray());
	}

	public void genKey(BigInteger p, BigInteger q, BigInteger e) {
		// 计算（p-1)*(q-1)
		BigInteger pq = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

		// 计算模数p*q
		BigInteger n = p.multiply(q);

		// 计算出d，即e的模n逆
		BigInteger d = e.modInverse(pq);

		// 公钥
		this.publicKey = e;

		// 私钥
		this.privateKey = d;

		// modulus
		this.modulus = n;
	}

	/**
	 * 产生指定长度的公钥和私钥
	 * 
	 * @param
	 */
	void genKey(int keyBitLength) {
        // 产生两个(N/2 - 1)位的大素数p和q
        BigInteger p = genProbablePrime(keyBitLength / 2 - 1);
        BigInteger q = genProbablePrime(keyBitLength / 2 - 1);

        // 随便找一个e
        BigInteger e = genProbablePrime(keyBitLength);

        //生成公钥和私钥
        genKey(p, q, e);
	}


    /**
     * 随机产生指定位数的素数
     * @param bitLength
     * @return
     */
    public static BigInteger genProbablePrime(int bitLength){
        return BigInteger.probablePrime(bitLength, random);
    }

    /**
     * 编码
     * @param bytes
     * @param key
     * @param modulus
     * @return
     */
	public static byte[] encode(byte[] bytes, BigInteger key, BigInteger modulus) {
		byte[] resultBytes =  new BigInteger(bytes).modPow(key, modulus).toByteArray();
        return resultBytes;
	}

    /**
     * 解码
     * @param bytes
     * @param key
     * @param modulus
     * @return
     */
	public static byte[] decode(byte[] bytes, BigInteger key, BigInteger modulus) {
		return new BigInteger(bytes).modPow(key, modulus).toByteArray();
	}

    private static void testKey() throws Exception {
        // 加密
        String source = "RSA公钥加密算法是1977年由Ron Rivest、Adi Shamirh和LenAdleman在（美国麻省理工学院）开发的。";

        BigInteger privateKey = new BigInteger("18B78BA4000E8B89CB767D2CCADBD26D19CE80F3A72262C6BCB1ACF5F838B1651F901AAFF953790C2DFC87D35DB6D92D0174DC397943F79DBA82DD613E51DBD191AC01F26EB68795849C6A912036BDFD17EA8F6BC2DC0E223B0F2F5487473273F6797C7D8037E25101ED5C180A7659E3B51EEB729310121A46238BD1EFC730AB",16);
        BigInteger publicKey = new BigInteger("B60432D2F12CD321D5F827E67DE1B5BCA51A13BEB59B9969E6A7E509F0442FF6FC9AE091E8B4B49B1A8AC275FF9A1922F4EDED500AE1E08ED64D2974BDA352A3",16);
        BigInteger modulus = new BigInteger("76E02BF454A147847E5C6D409D7D76750602853440E1A8CD0CBB97DFD3C874D4F028FE2485E6A618D3EB2E7483622200B6ED4DA12C8189417256375297A0CB97D81ED229242A87A6C9FE233B8AA3D7002E348CB859BC44C8591E33B5EFFC8827459D96BCEC30950A3F6D769FC29922C1B5FD4E1E248FBDA8A92BA8BDD91FBE59",16);

        System.out.println("privKey:" + privateKey.bitLength());
        System.out.println(privateKey);

        System.out.println("pubKey:" + publicKey.bitLength());
        System.out.println(publicKey);

        System.out.println("modulus:" + modulus.bitLength());
        System.out.println(modulus);

        System.out.println("============================");


        byte[] bytes = source.getBytes("UTF-8");
        System.out.println("[SOURCE BYTES]:" + bytes.length);
        byte[] cipher =RSAAlgorithm.encode(bytes,privateKey,modulus);
        System.out.println("[ENCODE BYTES]:" + cipher.length);
        System.out.println("[ENCODE]:\r\n" + Hex.encode(cipher));


        System.out.println("============================");
        // 解密
        byte[] plain = RSAAlgorithm.decode(cipher,publicKey,modulus);
        String decode =  new String(plain, "UTF-8");
        System.out.println("[DECODE BYTES]:" + plain.length);
        System.out.println("[DECODE]:\r\n" +decode);

        Asserts.assertEquals(source,decode);
    }

	private static void test() throws Exception {
//        BigInteger Q = new BigInteger(Hex.decode("8682236E835BAF3CFEC279F0C479558EF55EADA5836D1D1B18D6D85736EB71F263D440DC9E7E0A997BECDD05B60EC19D63AC71EC27D91269739453D5050A6783"));
//        BigInteger P =  new BigInteger(Hex.decode("E23F58C86F907C13FA8992556F9B912A1FF3C90E7216DACC320B9319AE4303562E9327FB8D614D30043A6EDC7F817BCE0C2D22DD973E2F4CE9D2E2D8730CFFF3"));
//        BigInteger E = new BigInteger(Hex.decode("B60432D2F12CD321D5F827E67DE1B5BCA51A13BEB59B9969E6A7E509F0442FF6FC9AE091E8B4B49B1A8AC275FF9A1922F4EDED500AE1E08ED64D2974BDA352A3"));

        BigInteger Q = RSAAlgorithm.genProbablePrime(511);
        BigInteger P =  RSAAlgorithm.genProbablePrime(511);
        BigInteger E =  RSAAlgorithm.genProbablePrime(1024);
        System.out.println(String.format("Q:%d , P:%d , E:%d", Q.bitLength(), P.bitLength(), E.bitLength()));


		RSAAlgorithm rsa = new RSAAlgorithm(P,Q,E);

        System.out.println("privKey:" + rsa.getPrivateKey().bitLength());
        System.out.println(rsa.getPrivateKey());

        System.out.println("pubKey:" + rsa.getPrivateKey().bitLength());
        System.out.println(rsa.getPrivateKey());

        System.out.println("modulus:" + rsa.getModulus().bitLength());
        System.out.println(rsa.getModulus());

        System.out.println("============================");


		// 加密
		String source = "RSA公钥加密算法是1977年由Ron Rivest、Adi Shamirh和LenAdleman在（美国麻省理工学院）开发的。";


		byte[] bytes = source.getBytes("UTF-8");
        System.out.println("[SOURCE BYTES]:" + bytes.length);
		byte[] cipher =RSAAlgorithm.encode(bytes,rsa.getPrivateKey(),rsa.getModulus());
        System.out.println("[ENCODE BYTES]:" + cipher.length);
		System.out.println("[ENCODE]:\r\n" + Hex.encode(cipher));


        System.out.println("============================");
		// 解密
		byte[] plain = RSAAlgorithm.decode(cipher,rsa.getPublicKey(),rsa.getModulus());
        String decode =  new String(plain, "UTF-8");
        System.out.println("[DECODE BYTES]:" + plain.length);
		System.out.println("[DECODE]:\r\n" +decode);

        Asserts.assertEquals(source,decode);
	}

    public static void main(String[] args) throws Exception {
        testKey();
    }
}

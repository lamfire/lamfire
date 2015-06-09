package com.lamfire.code;

import com.lamfire.utils.ArrayUtils;
import com.lamfire.utils.Bytes;
import com.lamfire.utils.IOUtils;

import java.io.ByteArrayOutputStream;
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
    public static final int KEYSIZE = 1024;
	static final int MAX_ENCRYPT_BLOCK = KEYSIZE / 8 - 11;
	static final int MAX_DECRYPT_BLOCK = KEYSIZE / 8;


    private int keySize = 1024;
	private BigInteger publicKey;
	private BigInteger privateKey;
	private BigInteger modulus;
    private int maxEncryptBlock;
    private int maxDecryptBlock;

	public RSAAlgorithm(int keySize) {
        this.keySize = keySize;
        maxEncryptBlock = keySize / 8 - 11;
        maxDecryptBlock = keySize / 8;
		genKey(keySize);
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
	public void genKey(int keySize) {
		// 产生两个N/2位的大素数p和q
		BigInteger p = BigInteger.probablePrime(keySize / 2, random);
		BigInteger q = BigInteger.probablePrime(keySize / 2, random);

		// 随便找一个e
		BigInteger e = BigInteger.probablePrime(keySize, random);

		//
		genKey(p, q, e);
	}

	public static byte[] encode(byte[] bytes, BigInteger key, BigInteger modulus) {
		return new BigInteger(bytes).modPow(key, modulus).toByteArray();
	}

	public static byte[] decode(byte[] bytes, BigInteger key, BigInteger modulus) {
		return new BigInteger(bytes).modPow(key, modulus).toByteArray();
	}

	public static void main(String[] args) throws Exception {
		RSAAlgorithm rsa = new RSAAlgorithm(1024);
		BigInteger privKey = rsa.getPrivateKey();
		BigInteger pubKey = rsa.getPublicKey();
		BigInteger modulus = rsa.getModulus();

        System.out.println("privKey:");
        System.out.println(privKey);

        System.out.println("pubKey:");
        System.out.println(pubKey);

        System.out.println("modulus:");
        System.out.println(modulus);

        System.out.println("============================");


		// 加密
		String source = "RSA公钥加密算法是1977年由Ron Rivest、Adi Shamirh和LenAdleman在（美国麻省理工学院）开发的。";


		byte[] bytes = source.getBytes("UTF-8");
		byte[] cipher =RSAAlgorithm.encode(bytes,rsa.getPrivateKey(),rsa.getModulus());
		System.out.println("\r\n[ENCODE]:\r\n" + Base64.encode(cipher));

        System.out.println("============================");
		// 解密
		byte[] plain = RSAAlgorithm.decode(cipher,rsa.getPublicKey(),rsa.getModulus());
		System.out.println("\r\n[DECODE]:\r\n" + new String(plain, "UTF-8"));
	}
}

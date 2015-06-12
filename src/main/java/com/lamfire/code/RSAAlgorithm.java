package com.lamfire.code;

import com.lamfire.utils.*;

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
    private int keyBitLength = 1024;
	private BigInteger publicKey;
	private BigInteger privateKey;
	private BigInteger modulus;
    private int encryptBlock;
    private int decryptBlock;

	public RSAAlgorithm(int keyBitLength) {
        setKeyBitLength(keyBitLength);
	}

	public RSAAlgorithm(int keyBitLength,BigInteger p, BigInteger q, BigInteger e) {
        setKeyBitLength(keyBitLength);
		genKey(p, q, e);
	}


    private void assertBlock(byte[] bytes){
         Asserts.equalsAssert(bytes.length, keyBitLength / 8);
    }


    public byte[] encode(byte[] source,BigInteger key,BigInteger modulus){
        int blockSize = encryptBlock;
        // 对数据分段加密
        int inputLen = source.length;
        int offSet = 0;
        byte[] cache;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            while (inputLen > offSet) {
                if (inputLen - offSet > blockSize) {
                    cache = encodeBlock(source, offSet, blockSize, key, modulus);
                } else {
                    cache = encodeBlock(source, offSet, inputLen - offSet, key, modulus);
                }
                assertBlock(cache);
                out.write(cache, 0, cache.length);
                offSet += blockSize;
            }
            byte[] encryptedData = out.toByteArray();
            return encryptedData;
         } finally {
            IOUtils.closeQuietly(out);
        }
    }

    public byte[] decode(byte[] source,BigInteger key,BigInteger modulus){
        int blockSize = decryptBlock;
        // 对数据分段解密
        int inputLen = source.length;
        int offSet = 0;
        byte[] cache;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            while (inputLen > offSet) {
                if (inputLen - offSet > blockSize) {
                    cache = decodeBlock(source,offSet,blockSize,key,modulus);
                } else {
                    cache = decodeBlock(source,offSet,inputLen - offSet,key,modulus);
                }
                out.write(cache, 0, cache.length);
                offSet += blockSize;
            }
            byte[] decryptedData = out.toByteArray();
            return decryptedData;
        }finally {
            IOUtils.closeQuietly(out);
        }
    }

    public void setKeyBitLength(int keyBitLength){
        this.keyBitLength = keyBitLength;
        this.encryptBlock = keyBitLength / 8 - 11;
        this.decryptBlock = keyBitLength / 8;
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

    public void genKey(){
        genKey(keyBitLength);
    }

	private void genKey(BigInteger p, BigInteger q, BigInteger e) {
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
    private void genKey(int keyBitLength) {
        setKeyBitLength(keyBitLength);

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
	protected static byte[] encodeBlock(final byte[] bytes, BigInteger key, BigInteger modulus) {
        BigInteger message = new BigInteger(bytes);
        if(message.compareTo(modulus) > 0){
              throw new RuntimeException("Max.length(byte[]) of message can be (keyBitLength/8-1),to make sure that M < N.");
        }
        System.out.println("[S]:"+Hex.encode(bytes));
		byte[] resultBytes =  message.modPow(key, modulus).toByteArray();
        System.out.println("[E]:"+Hex.encode(resultBytes));
        return resultBytes;
	}

    protected static byte[] encodeBlock(final byte[] bytes,int startIndex,int length, BigInteger key, BigInteger modulus) {
        byte[] source = Bytes.subBytes(bytes,startIndex,length);
        return encodeBlock(source,key,modulus);
    }

    /**
     * 解码
     * @param bytes
     * @param key
     * @param modulus
     * @return
     */
    protected static byte[] decodeBlock(byte[] bytes, BigInteger key, BigInteger modulus) {
        System.out.println("[E]:"+Hex.encode(bytes));
		byte[] decodeBytes =  new BigInteger(bytes).modPow(key, modulus).toByteArray();
        System.out.println("[D]:"+Hex.encode(decodeBytes));
        return decodeBytes;
	}

    protected static byte[] decodeBlock(final byte[] bytes,int startIndex,int length, BigInteger key, BigInteger modulus) {
        byte[] source = Bytes.subBytes(bytes,startIndex,length);
        return decodeBlock(source, key, modulus);
    }

}

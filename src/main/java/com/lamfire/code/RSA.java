package com.lamfire.code;

import com.lamfire.utils.IOUtils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA算法工具
 * 
 * @author lamfire
 * 
 */
public class RSA {
	/**
	 * 加密算法RSA
	 */
    public static final String KEY_ALGORITHM = "RSA";

	/**
	 * 签名算法
	 */
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * 密钥长度
     */
    private int keySize = 1024;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private String algorithm = KEY_ALGORITHM;

    public RSA(int keySize)throws Exception {
        this.keySize = keySize;
        KeyPair keyPair = genKeyPair(keySize, algorithm);
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }

    public RSA(int keySize, String algorithm) throws Exception {
        this.keySize = keySize;
        KeyPair keyPair = genKeyPair(keySize, algorithm);
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
        this.algorithm = algorithm;
    }

    public RSA(int keySize, byte[] privateKey, byte[] publicKey) throws Exception {
        this.keySize = keySize;
        this.privateKey = toPrivateKey(privateKey);
        this.publicKey = toPublicKey(publicKey);
    }

    public RSA(int keySize, PrivateKey privateKey,PublicKey publicKey) {
        this.keySize = keySize;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public int getKeySize() {
        return keySize;
    }

    public void setKeySize(int keySize) {
        this.keySize = keySize;
    }

    public Key getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(byte[] privateKey)throws Exception {
        this.privateKey = toPrivateKey(privateKey, algorithm);
    }

    public void setPrivateKey(PrivateKey privateKey){
        this.privateKey = (privateKey);
    }

    public Key getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) throws Exception{
        this.publicKey = toPublicKey(publicKey, algorithm);
    }

    public void setPublicKey(PublicKey publicKey){
        this.publicKey = (publicKey);
    }


	/**
	 * 用私钥对信息生成数字签名
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String signatureAsBase64(byte[] data) throws Exception {
		return Base64.encode(signature(data,privateKey));
	}

    public byte[] signature(byte[] data) throws Exception {
        return (signature(data,privateKey));
    }

    /**
     * 校验数字签名
     * @param data
     * @param signWithBase64
     * @return
     * @throws Exception
     */
    public boolean verifySignatureWithBase64(byte[] data,String signWithBase64) throws Exception {
        return verifySignature(data,Base64.decode(signWithBase64),publicKey);
    }

    public boolean verifySignature(byte[] data,byte[] signBytes) throws Exception {
        return verifySignature(data,signBytes,publicKey);
    }

    /**
     * 私钥解密
     * @param data
     * @return
     * @throws Exception
     */

    public byte[] decodeByPrivateKey(byte[] data) throws Exception {
        return decode(data, privateKey, keySize, algorithm);
    }

    /**
     * 公钥解密
     * @param data
     * @return
     * @throws Exception
     */
    public byte[] decodeByPublicKey(byte[] data) throws Exception {
        return decode(data, publicKey, keySize, algorithm);
    }



    /**
     * 公钥加密
     * @param data
     * @return
     * @throws Exception
     */
    public  byte[] encodeByPublicKey(byte[] data) throws Exception {
        return encode(data, publicKey, keySize, algorithm);
    }

    /**
     * 私钥加密
     * @param data
     * @return
     * @throws Exception
     */
    public byte[] encodeByPrivateKey(byte[] data) throws Exception {
        return encode(data, privateKey, keySize, algorithm);
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public static KeyPair genKeyPair(int keySize, String algorithm) throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(algorithm);
        keyPairGen.initialize(keySize);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        return keyPair;
    }

    /**
     * 生成密钥对(公钥和私钥)
     * @return
     * @throws Exception
     */
    public static KeyPair genKeyPair(int keySize) throws Exception {
        return genKeyPair(keySize, KEY_ALGORITHM);
    }

    /**
	 * 用私钥对信息生成数字签名
	 * @param data
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static byte[] signature(byte[] data, PrivateKey privateKey) throws Exception {
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(privateKey);
		signature.update(data);
		return signature.sign();
	}


	
	/**
	 * 校验数字签名
	 * @param data
	 * @param sign
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static boolean verifySignature(byte[] data, byte[] sign,byte[] publicKey) throws Exception {
		PublicKey publicK = toPublicKey(publicKey);
		return verifySignature(data,sign,publicK);
	}
	
	/**
	 * 校验数字签名
	 * @param data
	 * @param sign
	 * @param publicK
	 * @return
	 * @throws Exception
	 */
	public static boolean verifySignature(byte[] data, byte[] sign,PublicKey publicK) throws Exception {
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(publicK);
		signature.update(data);
		return signature.verify(sign);
	}


    public static byte[] decode(byte[] data, Key key, int keySize) throws Exception {
        return decode(data, key, keySize, KEY_ALGORITHM);
    }

	/**
	 * 解密函数
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
    public static byte[] decode(byte[] data, Key key, int keySize, String algorithm) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.DECRYPT_MODE, key);

        int blockSize = keySize / 8;
		// 对数据分段解密
		int inputLen = data.length;
		int offSet = 0;
		byte[] cache;
		int i = 0;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			while (inputLen > offSet) {
				if (inputLen - offSet > blockSize) {
					cache = cipher.doFinal(data, offSet, blockSize);
				} else {
					cache = cipher.doFinal(data, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * blockSize;
			}
			byte[] decryptedData = out.toByteArray();
			return decryptedData;
		} catch (Exception e) {
			throw e;
		} finally {
			IOUtils.closeQuietly(out);
		}

	}

    public static byte[] encode(byte[] data, Key key, int keySize) throws Exception {
        return encode(data, key, keySize, KEY_ALGORITHM);
    }

	/**
	 * 加密函数
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
    public static byte[] encode(byte[] data, Key key, int keySize, String algorithm) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.ENCRYPT_MODE, key);

        int blockSize = keySize / 8 - 11;

		// 对数据分段加密
		int inputLen = data.length;
		int offSet = 0;
		byte[] cache;
		int i = 0;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			while (inputLen > offSet) {
				if (inputLen - offSet > blockSize) {
					cache = cipher.doFinal(data, offSet, blockSize);
				} else {
					cache = cipher.doFinal(data, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * blockSize;
			}
			byte[] encryptedData = out.toByteArray();
			return encryptedData;
		} catch (Exception e) {
			throw e;
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

	/**
	 * 获取私钥
	 * @param keyPair
	 * @return
	 * @throws Exception
	 */
	public static byte[] getPrivateKey(KeyPair keyPair) throws Exception {
		Key key = keyPair.getPrivate();
		return (key.getEncoded());
	}

	/**
	 * 获取公钥
	 * @param keyPair
	 * @return
	 * @throws Exception
	 */
	public static  byte[] getPublicKey(KeyPair keyPair) throws Exception {
		Key key = keyPair.getPublic();
		return (key.getEncoded());
	}
	
	/**
	 * 获取Modulus
	 * @param keyPair
	 * @return
	 */
	public static  byte[] getModulus(KeyPair keyPair){
		RSAPrivateKey key = (RSAPrivateKey)keyPair.getPrivate();
		return (key.getModulus().toByteArray());
	}
	
	/**
	 * 获取Modulus
	 * @param key
	 * @return
	 */
	public static  byte[] getModulus(RSAPrivateKey key){
		return (key.getModulus().toByteArray());
	}
	
	/**
	 * 获取Modulus
	 * @param key
	 * @return
	 */
	public static  byte[] getModulus(RSAPublicKey key){
		return (key.getModulus().toByteArray());
	}
	
	/**
	 * 解码私钥
	 * @param keyBytes
	 * @return
	 * @throws Exception
	 */
    public static RSAPrivateKey toPrivateKey(byte[] keyBytes, String algorithm) throws Exception {
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		RSAPrivateKey key = (RSAPrivateKey)keyFactory.generatePrivate(keySpec);
		return key;
	}

    public static RSAPrivateKey toPrivateKey(byte[] keyBytes) throws Exception {
        return toPrivateKey(keyBytes, KEY_ALGORITHM);
    }
	
	/**
	 * 解码公钥
	 * @param keyBytes
	 * @return
	 * @throws Exception
	 */
    public static RSAPublicKey toPublicKey(byte[] keyBytes, String algorithm) throws Exception {
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		RSAPublicKey key = (RSAPublicKey)keyFactory.generatePublic(keySpec);
		return key;
	}

    public static RSAPublicKey toPublicKey(byte[] keyBytes) throws Exception {
        return toPublicKey(keyBytes, KEY_ALGORITHM);
    }


}

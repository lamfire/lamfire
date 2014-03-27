package com.lamfire.code;

import java.io.ByteArrayOutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.lamfire.utils.IOUtils;

/**
 * RSA算法工具
 * 
 * @author admin
 * 
 */
public class RSA {
	/**
	 * 加密算法RSA
	 */
	public static final String KEY_ALGORITHM = "RSA";

	/**
	 * 密钥长度
	 */
	public static final int KEYSIZE = 1024;

	/**
	 * 签名算法
	 */
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

	/**
	 * RSA最大加密明文大小
	 */
	private static final int MAX_ENCRYPT_BLOCK = 117;

	/**
	 * RSA最大解密密文大小
	 */
	private static final int MAX_DECRYPT_BLOCK = 128;

	/**
	 * 生成密钥对(公钥和私钥)
	 * @return
	 * @throws Exception
	 */
	public static KeyPair genKeyPair() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(KEYSIZE);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		return keyPair;
	}

	/**
	 * 用私钥对信息生成数字签名
	 * @param data
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static String signature(byte[] data, String privateKey) throws Exception {
		PrivateKey privateK = toPrivateKey(privateKey);
		return Base64.encode(signature(data,privateK));
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
	 * @param publicKey
	 * @param sign
	 * @return
	 * @throws Exception
	 */
	public static boolean verifySignature(byte[] data,String sign, String publicKey) throws Exception {
		return verifySignature(data,Base64.decode(sign),publicKey);
	}
	
	/**
	 * 校验数字签名
	 * @param data
	 * @param sign
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static boolean verifySignature(byte[] data, byte[] sign,String publicKey) throws Exception {
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

	/**
	 * 私钥解密
	 * @param data
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	 
	public static byte[] decodeByPrivateKey(byte[] data, String privateKey) throws Exception {
		Key privateK = toPrivateKey(privateKey);;
		return decode(data, privateK);
	}

	/**
	 * 公钥解密
	 * @param data
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static byte[] decodeByPublicKey(byte[] data, String publicKey) throws Exception {
		Key publicK = toPublicKey(publicKey);
		return decode(data, publicK);
	}

	/**
	 * 解密函数
	 * 
	 * @param data
	 * @param publicK
	 * @return
	 * @throws Exception
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws Exception
	 */
	public static byte[] decode(byte[] data, Key key) throws Exception {
		Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key);

		// 对数据分段解密
		int inputLen = data.length;
		int offSet = 0;
		byte[] cache;
		int i = 0;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			while (inputLen > offSet) {
				if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
					cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(data, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_DECRYPT_BLOCK;
			}
			byte[] decryptedData = out.toByteArray();
			out.close();
			return decryptedData;
		} catch (Exception e) {
			throw e;
		} finally {
			IOUtils.closeQuietly(out);
		}

	}

	/**
	 * 公钥加密
	 * @param data
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static byte[] encodeByPublicKey(byte[] data, String publicKey) throws Exception {
		Key publicK = toPublicKey(publicKey);
		return encode(data, publicK);
	}

	/**
	 * 私钥加密
	 * @param data
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static byte[] encodeByPrivateKey(byte[] data, String privateKey) throws Exception {
		Key privateK = toPrivateKey(privateKey);
		return encode(data, privateK);
	}

	/**
	 * 加密函数
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encode(byte[] data, Key key) throws Exception {
		Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key);

		// 对数据分段加密
		int inputLen = data.length;
		int offSet = 0;
		byte[] cache;
		int i = 0;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			while (inputLen > offSet) {
				if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
					cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(data, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_ENCRYPT_BLOCK;
			}
			byte[] encryptedData = out.toByteArray();
			out.close();
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
	public static String getPrivateKey(KeyPair keyPair) throws Exception {
		Key key = keyPair.getPrivate();
		return Base64.encode(key.getEncoded());
	}

	/**
	 * 获取公钥
	 * @param keyPair
	 * @return
	 * @throws Exception
	 */
	public static String getPublicKey(KeyPair keyPair) throws Exception {
		Key key = keyPair.getPublic();
		return Base64.encode(key.getEncoded());
	}
	
	/**
	 * 获取Modulus
	 * @param keyPair
	 * @return
	 */
	public static String getModulus(KeyPair keyPair){
		RSAPrivateKey key = (RSAPrivateKey)keyPair.getPrivate();
		return Base64.encode(key.getModulus().toByteArray());
	}
	
	/**
	 * 获取Modulus
	 * @param key
	 * @return
	 */
	public static String getModulus(RSAPrivateKey key){
		return Base64.encode(key.getModulus().toByteArray());
	}
	
	/**
	 * 获取Modulus
	 * @param key
	 * @return
	 */
	public static String getModulus(RSAPublicKey key){
		return Base64.encode(key.getModulus().toByteArray());
	}
	
	/**
	 * 解码私钥
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static RSAPrivateKey toPrivateKey(String privateKey)throws Exception{
		byte[] keyBytes = Base64.decode(privateKey);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		RSAPrivateKey key = (RSAPrivateKey)keyFactory.generatePrivate(keySpec);
		return key;
	}
	
	/**
	 * 解码公钥
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static RSAPublicKey toPublicKey(String publicKey)throws Exception{
		byte[] keyBytes = Base64.decode(publicKey);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		RSAPublicKey key = (RSAPublicKey)keyFactory.generatePublic(keySpec);
		return key;
	}
}

package com.lamfire.code;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;

public class HmacSHA1 {
	private static final String Algorithm = "HmacSHA1";
	private KeyGenerator kg = null;
	private Mac mac = null;
	
	private static HmacSHA1 instance = null;
	
	public static HmacSHA1 getInstance(){
		if(instance == null){
			try {
				instance = new HmacSHA1();
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
		}
		return instance;
	}
	
	private HmacSHA1() throws NoSuchAlgorithmException{
		kg = KeyGenerator.getInstance(Algorithm);
		mac = Mac.getInstance(Algorithm);
	}

	public byte[] digest(byte[] source,byte[] key) throws  InvalidKeyException{
		kg.init(new SecureRandom(key));
		SecretKey sk = kg.generateKey();
		
		mac.init(sk);
		byte[] result = mac.doFinal(source);
		return result;
	}

	public String digest(String source,String key) throws InvalidKeyException, NoSuchAlgorithmException{
		return Hex.encode(digest(source.getBytes(),key.getBytes()));
	}
	
	public static byte[] hash(byte[] source,byte[] key){
		HmacSHA1 hcacSha1 = HmacSHA1.getInstance();
		try {
			return hcacSha1.digest(source, key);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String hash(String source,String key){
		byte[] bytes = hash(source.getBytes(),key.getBytes());
		return Hex.encode(bytes);
	}
}

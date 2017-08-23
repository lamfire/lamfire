package com.lamfire.code;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class AlgorithmSignature {
	public static final String RSA_SIGN_ALGORITHMS = "SHA1WithRSA";
	public static final String DSA_SIGN_ALGORITHMS = "SHA1withDSA";

	public static byte[] signSHA1WithDSA(PrivateKey privateKey,byte[] data) throws SignatureException, InvalidKeyException, NoSuchAlgorithmException{
		Signature signature = Signature.getInstance(DSA_SIGN_ALGORITHMS);
		signature.initSign(privateKey);
		signature.update(data);
		return signature.sign();
	}
	
	public static boolean verifySHA1WithDSA(PublicKey pubkey,byte[] data,byte[] signature) throws SignatureException, InvalidKeyException, NoSuchAlgorithmException{
		Signature sign = Signature.getInstance(DSA_SIGN_ALGORITHMS);
		sign.initVerify(pubkey);
		sign.update(data);
		return sign.verify(signature);
	}


	public static byte[] signSHA1WithRSA(PrivateKey privateKey,byte[] data) throws SignatureException, InvalidKeyException, NoSuchAlgorithmException{
		Signature signature = Signature.getInstance(RSA_SIGN_ALGORITHMS);
		signature.initSign(privateKey);
		signature.update(data);
		return signature.sign();
	}

	public static boolean verifySHA1WithRSA(PublicKey pubkey,byte[] data,byte[] signature) throws SignatureException, InvalidKeyException, NoSuchAlgorithmException{
		Signature sign = Signature.getInstance(RSA_SIGN_ALGORITHMS);
		sign.initVerify(pubkey);
		sign.update(data);
		return sign.verify(signature);
	}

	public static byte[] signSHA1WithRSA(byte[] privateKey,byte[] data) throws SignatureException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
		PKCS8EncodedKeySpec pksc8 = new PKCS8EncodedKeySpec(privateKey);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey priKey = keyFactory.generatePrivate(pksc8);
		return signSHA1WithRSA(priKey,data);
	}

	public static boolean verifySHA1WithRSA(byte[] pubkeyBytes,byte[] data,byte[] signature) throws SignatureException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(pubkeyBytes));
		return verifySHA1WithRSA(pubKey,data,signature);
	}

}

package com.lamfire.code;

import com.lamfire.logger.Logger;

import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class ECDSA {
    private static final Logger LOGGER = Logger.getLogger("ECDSA");
    private static final String KeyAlgorithm = "EC";
    private static final String SignAlgorithm = "SHA1withECDSA";
    private int keySize = 256;
    private ECPublicKey ecPublicKey ;
    private ECPrivateKey ecPrivateKey ;

    public ECDSA(int keySize){
        try {
            this.keySize = keySize;
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KeyAlgorithm);
            keyPairGenerator.initialize(keySize);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            ecPublicKey = (ECPublicKey) keyPair.getPublic();
            ecPrivateKey = (ECPrivateKey) keyPair.getPrivate();
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
        }
    }

    public byte[] sign(byte[] data)throws SignatureException{
        return sign(data,ecPrivateKey.getEncoded());
    }

    public boolean verify(byte[] data,byte[] sign){
        return verify(data,sign,ecPublicKey.getEncoded());
    }


    public static KeyPair getKeyPair(int keySize){
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(KeyAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyPairGenerator.initialize(keySize);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }


    public static byte[] sign(byte[] data,byte[] ecPrivateKey)throws SignatureException{
        // 签名
        try {
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(ecPrivateKey);
            KeyFactory keyFactory = KeyFactory.getInstance(KeyAlgorithm);
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Signature signature = Signature.getInstance(SignAlgorithm);
            signature.initSign(privateKey);
            signature.update(data);
            byte[] sign = signature.sign();
            return sign;
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
            throw new SignatureException(e);
        }

    }

    public static boolean verify(byte[] data,byte[] sign,byte[] ecPublicKey){
        // 验证签名
        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(ecPublicKey);
            KeyFactory keyFactory = KeyFactory.getInstance(KeyAlgorithm);
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            Signature signature = Signature.getInstance(SignAlgorithm);
            signature.initVerify(publicKey);
            signature.update(data);
            return signature.verify(sign);
        }catch (Exception e){
            LOGGER.error(e.getMessage(),e);
        }
        return false;
    }

    public ECPublicKey getEcPublicKey() {
        return ecPublicKey;
    }

    public ECPrivateKey getEcPrivateKey() {
        return ecPrivateKey;
    }
}

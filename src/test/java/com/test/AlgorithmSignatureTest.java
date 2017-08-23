package com.test;

import com.lamfire.code.AlgorithmSignature;
import com.lamfire.code.Hex;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by linfan on 2017/8/23.
 */
public class AlgorithmSignatureTest {
    public static void main(String[] args) throws Exception {

        KeyPairGenerator keygen = KeyPairGenerator.getInstance("DSA");
        keygen.initialize(512);
        KeyPair keypair = keygen.generateKeyPair();
        PublicKey pubKey = keypair.getPublic();
        PrivateKey privKey = keypair.getPrivate();


        byte[] signature = AlgorithmSignature.signSHA1WithDSA(privKey, "admin".getBytes());
        System.out.println(Hex.encode(signature));
        boolean ret = AlgorithmSignature.verifySHA1WithDSA(pubKey, "admin".getBytes(), signature);
        System.out.println(ret);



        keygen = KeyPairGenerator.getInstance("RSA");
        keygen.initialize(512);
        keypair = keygen.generateKeyPair();
        pubKey = keypair.getPublic();
        privKey = keypair.getPrivate();


        signature = AlgorithmSignature.signSHA1WithRSA(privKey, "admin".getBytes());
        System.out.println(Hex.encode(signature));
        ret = AlgorithmSignature.verifySHA1WithRSA(pubKey, "admin".getBytes(), signature);
        System.out.println(ret);

        signature = AlgorithmSignature.signSHA1WithRSA(privKey.getEncoded(), "admin".getBytes());
        System.out.println(Hex.encode(signature));
        ret = AlgorithmSignature.verifySHA1WithRSA(pubKey.getEncoded(), "admin".getBytes(), signature);
        System.out.println(ret);

    }
}

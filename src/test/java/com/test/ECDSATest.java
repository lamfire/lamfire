package com.test;

import com.lamfire.code.Base64;
import com.lamfire.code.ECDSA;

import java.security.SignatureException;

public class ECDSATest {

    public static void main(String[] args) throws SignatureException {
        ECDSA ecdsa = new ECDSA(256);
        String source = "lamfire.com";

        System.out.println("[private]: "+ Base64.encode(ecdsa.getEcPrivateKey().getEncoded()));
        System.out.println("[public]: "+Base64.encode(ecdsa.getEcPublicKey().getEncoded()));

        byte[] sign  = ecdsa.sign(source.getBytes());
        System.out.println("[sign]: "+Base64.encode(sign));
        System.out.println("[verify]: " + ecdsa.verify(source.getBytes(),sign));
    }

}

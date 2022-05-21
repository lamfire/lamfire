package com.test;

import com.lamfire.code.FireCa;
import com.lamfire.code.RSA;
import com.lamfire.json.JSON;
import com.lamfire.utils.RandomUtils;

public class FireCaTest {
    public static void main(String[] args) throws Exception{
        RSA rsa = new RSA(1024);

        JSON data = new JSON();
        data.put("id","U9802342388G");
        data.put("memo", RandomUtils.randomTextWithFixedLength(256));
        data.put("date",System.currentTimeMillis());
        System.out.println("Data : " + data);

        byte[] caBytes = FireCa.encode(data.toBytes(),rsa.getPrivateKey().getEncoded());
        System.out.println("CA length : " + caBytes.length);

        byte[] dataBytes = FireCa.decode(caBytes,rsa.getPublicKey().getEncoded());
        System.out.println("Data length : " + dataBytes.length);

        System.out.println("Decode : " + JSON.fromBytes(dataBytes));
    }
}

package com.test;

import com.lamfire.code.Base64;
import com.lamfire.code.CaCodec;
import com.lamfire.code.RSA;
import com.lamfire.json.JSON;
import com.lamfire.utils.RandomUtils;

public class CaCodecTest {
    public static void main(String[] args) throws Exception{
        RSA rsa = new RSA(1024);

        JSON data = new JSON();
        data.put("id","U9802342388G");
        data.put("memo", RandomUtils.randomTextWithFixedLength(28));
        data.put("date",System.currentTimeMillis());
        System.out.println("Data : " + data);
        System.out.println("Base64 : " + Base64.encode(data.toBytes()));

        byte[] dataBytes = data.toBytes();
        System.out.println("Data Len : " +dataBytes.length);

        byte[] caBytes = CaCodec.encode(dataBytes,rsa.getPrivateKey().getEncoded());
        System.out.println("CA length : " + caBytes.length);

        dataBytes = CaCodec.decode(caBytes,rsa.getPublicKey().getEncoded());
        System.out.println("Data length : " + dataBytes.length);

        System.out.println("Decode : " + JSON.fromBytes(dataBytes));
    }
}

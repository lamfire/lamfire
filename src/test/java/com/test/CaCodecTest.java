package com.test;

import com.lamfire.code.CaCodec;
import com.lamfire.code.RSA;
import com.lamfire.json.JSON;
import com.lamfire.utils.RandomUtils;

public class CaCodecTest {
    public static void main(String[] args) throws Exception{
        RSA rsa = new RSA(1024);

        JSON data = new JSON();
        data.put("id","U9802342388G");
        data.put("memo", "111");
        data.put("date",456115454);
        System.out.println("Data : " + data);

        byte[] dataBytes = data.toBytes();
        System.out.println("Data Len : " +dataBytes.length);

        byte[] caBytes = CaCodec.encode(dataBytes,rsa.getPrivateKey().getEncoded());
        System.out.println("CA length : " + caBytes.length);

        dataBytes = CaCodec.decode(caBytes,rsa.getPublicKey().getEncoded());
        System.out.println("Data length : " + dataBytes.length);

        System.out.println("Decode : " + JSON.fromBytes(dataBytes));
    }
}

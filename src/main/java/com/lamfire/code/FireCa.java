package com.lamfire.code;

import com.lamfire.json.JSON;
import com.lamfire.utils.RandomUtils;
import com.lamfire.utils.ZipUtils;

import java.security.Key;

public class FireCa {
    public static final int VERSION = 1;
    public static final int KEY_BITS = 1024;

    private FireCa(){}

    public static byte[] encode(byte[] data,byte[] privateKey) throws Exception {
        String key = RandomUtils.randomTextWithFixedLength(16);//aes key
        AES aes = new AES(key.getBytes());
        byte[] enBytes = aes.encode(data);
        Key rsaPrivateKey = RSA.toPrivateKey(privateKey);
        byte[] enKeyBytes = RSA.encode(key.getBytes(),rsaPrivateKey,KEY_BITS);
        String id = PUID.makeAsString();
        long time = System.currentTimeMillis();

        JSON json = new JSON();
        json.put("data",Base64.encodeBytes(enBytes));
        json.put("key",Base64.encodeBytes(enKeyBytes));
        json.put("date",time);
        json.put("version",VERSION);
        json.put("id",id);

        byte[] caBytes = json.toBytes();
        byte[] zipCaBytes = ZipUtils.zip(caBytes);
        return zipCaBytes;
    }

    public static JSON decodeCa(byte[] zipCaBytes,byte[] publicKey) throws Exception {
        byte[] bytes = ZipUtils.unzip(zipCaBytes);
        JSON json = JSON.fromBytes(bytes);
        if(json.getInteger("version") != VERSION){
            throw new Exception("Tha ca version mismatch");
        }
        Key rsaPublicKey = RSA.toPublicKey(publicKey);
        byte[] aesKeyBytes = RSA.decode(Base64.decode(json.getString("key")),rsaPublicKey,KEY_BITS);
        AES aes = new AES(aesKeyBytes);
        byte[] data = aes.decode(Base64.decode(json.getString("data")));
        json.put("data",Base64.encodeBytes(data));
        return json;
    }

    public static byte[] decode(byte[] zipCaBytes,byte[] publicKey)throws Exception{
        JSON caJSON = decodeCa(zipCaBytes,publicKey);
        return Base64.decode(caJSON.getString("data"));
    }
}

package com.lamfire.code;

import com.lamfire.json.JSON;
import com.lamfire.utils.RandomUtils;
import com.lamfire.utils.StringUtils;
import com.lamfire.utils.ZipUtils;

import java.security.Key;

public class CaCodec {
    public static final int VERSION = 2;
    public static final int KEY_BITS = 1024;

    private CaCodec(){}

    public static byte[] encode(final byte[] data,final byte[] privateKey) throws Exception {
        String key = RandomUtils.randomTextWithFixedLength(16);//aes key
        byte[] dataHash = SHA1.digest(data);
        String hash = Hex.encode(dataHash);
        AES aes = new AES(key.getBytes());
        String data2Str = Base64.encode(data);
        byte[] dataStrBytes = data2Str.getBytes();
        byte[] enBytes = aes.encode(dataStrBytes);


        Key rsaPrivateKey = RSA.toPrivateKey(privateKey);
        byte[] enKeyBytes = RSA.encode(key.getBytes(),rsaPrivateKey,KEY_BITS);
        String id = PUID.makeAsString();
        long time = System.currentTimeMillis();

        JSON json = new JSON();
        json.put("data",Base64.encodeBytes(enBytes));
        json.put("key",Base64.encodeBytes(enKeyBytes));
        json.put("hash",hash);
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
        String hash = json.getString("hash");
        String key = json.getString("key");

        Key rsaPublicKey = RSA.toPublicKey(publicKey);
        byte[] aesKeyBytes = RSA.decode(Base64.decode(key),rsaPublicKey,KEY_BITS);
        AES aes = new AES(aesKeyBytes);
        byte[] data = aes.decode(Base64.decode(json.getString("data")));
        String data2str = new String(data).trim();
        byte[] srcData = Base64.decode(data2str);
        String encodedDataHash = Hex.encode(SHA1.digest(srcData));
        if(!StringUtils.equalsIgnoreCase(hash,encodedDataHash)){
            throw new Exception("Tha ca hash mismatch:" +encodedDataHash +" - " +hash);
        }
        json.put("data",Base64.encodeBytes(srcData));
        return json;
    }

    public static byte[] decode(byte[] zipCaBytes,byte[] publicKey)throws Exception{
        JSON caJSON = decodeCa(zipCaBytes,publicKey);
        return Base64.decode(caJSON.getString("data"));
    }
}

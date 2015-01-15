package com.test;

import com.lamfire.code.AES;
import com.lamfire.code.GUIDGen;
import com.lamfire.code.Hex;
import com.lamfire.code.HmacSHA1;
import com.lamfire.code.PUID;
import com.lamfire.code.Rijndael;
import com.lamfire.code.UUIDGen;

public class Client {

	public static void testRijndael(){
		String key = "1234567890123456";
		String data = "1234567890123456";
		Rijndael rijndael = new Rijndael();
		rijndael.makeKey(key.getBytes(), 128);
		byte[] datas = data.getBytes();
		byte[] ct = new byte[32];
		rijndael.encrypt(datas, ct);
		System.out.println(Hex.encode(ct));
	}
	
	public static void testGUID(){
		System.out.println(GUIDGen.guid());
	}
	
	public static void testAES(){
		String src = "1234567890123456";
		String password = "1234567890123456";
		String en = AES.encode(src, password);
		System.out.println("EN:" + en);
		
		String de = AES.decode(en, password);
		System.out.println("DE:"+de);
	}
	
	public static void testGuidGenerator(){
		String guid = GUIDGen.guid();
		System.out.println("guid:" + guid);
	}
	
	public static void testUuid(){
		String guid = UUIDGen.uuid();
		System.out.println("uuid:" + guid);
	}
	
	public static void testHmacSha1(String source, String key){
		String hash = HmacSHA1.hash(source, key);
		System.out.println(hash);
	}
	
	public static void main(String[] args) throws Exception {
		//testAES();
		//testRijndael();
		//testGUID();
		//testGuidGenerator();
		//testUuid();
		//testHmacSha1();
//		
//		51613ff4db48bf96929ecdda
//		51613ff4db48bf96929ecddb
//		51613ff4db48bf96929ecddc
		
		System.out.println(PUID.puidAsString());
		System.out.println(PUID.puidAsString());
		System.out.println(PUID.puidAsString());
	}
}

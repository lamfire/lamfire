package com.test;

import com.lamfire.code.AES;
import com.lamfire.code.GUIDGen;
import com.lamfire.code.Hex;
import com.lamfire.code.HmacSHA1;
import com.lamfire.code.PUID;
import com.lamfire.code.Rijndael;
import com.lamfire.code.UUIDGen;
import com.lamfire.utils.ClassLoaderUtils;
import com.lamfire.utils.FileUtils;
import com.lamfire.utils.IOUtils;
import com.lamfire.utils.OPSMonitor;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.Reader;
import java.util.Iterator;

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
	
	public static void testHmacSha1(String source, String key)throws Exception{
		String hash = HmacSHA1.hash(source, key);
		System.out.println(hash);
	}
	
	public static void main(String[] args) throws Exception {


		File file = ClassLoaderUtils.getResourceAsFile("keyword",Client.class);

        //ops.done();
        //System.out.println("ExpendTimeNano : " +ops.getLastExpendTimeNano());
        //System.out.println("ExpendTimeMillis : " +ops.getLastExpendTimeMillis());



        Reader reader = FileUtils.openFileReader(file);
        Iterator<String> lines =  IOUtils.readLineIterator(reader);
        OPSMonitor ops = new OPSMonitor("id001");
        ops.done();
        long startAt = System.nanoTime();
        while(lines.hasNext()){
            System.out.println(lines.next());
            ops.done();
        }
        System.out.println(System.nanoTime() - startAt);
        reader.close();

        System.out.println("TotalExpendTimeNano : " +ops.getTotalExpendTimeNano());
        System.out.println("TotalExpendTimeMillis : " +ops.getTotalExpendTimeMillis());

        System.out.println("MaxExpendTimeNano : " +ops.getMaxExpendTimeNano());
        System.out.println("MaxExpendTimeMillis : " +ops.getMaxExpendTimeMillis());

        System.out.println("ExpendTimeNano : " +ops.getLastExpendTimeNano());
        System.out.println("ExpendTimeMillis : " +ops.getLastExpendTimeMillis());

        System.out.println("AVGExpendTimeNano : " +ops.getAvgExpendTimeNano());
        System.out.println("AVGExpendTimeMillis : " +ops.getAvgExpendTimeMillis());

	}
}

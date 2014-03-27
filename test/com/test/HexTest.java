package com.test;

import com.lamfire.code.Hex;
import com.lamfire.utils.Asserts;

public class HexTest {
	
	public void testShort(){
		short source = 199;
		String en = Hex.encode(source);
		short de = Hex.toShort(en);
		Asserts.assertEquals(de, source);
	}
	
	public void testInt(){
		int source = 65535;
		String en = Hex.encode(source);
		int de = Hex.toInt(en);
		Asserts.assertEquals(de, source);
	}
	
	public void testLong(){
		long source = Long.MAX_VALUE;
		String en = Hex.encode(source);
		long de = Hex.toLong(en);
		Asserts.assertEquals(de, source);
	}
	
	public void testEncode(){
		String v = "1234567890qwertyuiop";
		byte[] bytes = v.getBytes();
		
		String s = Hex.encode(bytes,0,10);
		System.out.println(s);
		byte[] d = Hex.decode(s);
		System.out.println(new String(d));
		
	}

	public static void main(String[] args) {
		HexTest test = new HexTest();
		test.testShort();
		test.testInt();
		test.testLong();
		
		test.testEncode();
	}
}

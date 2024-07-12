package com.test;

import com.lamfire.code.Base62;
import com.lamfire.code.MD5;
import com.lamfire.utils.Bytes;

public class Base62Test {
	static String base64,base62;
	
	public static void main(String[] args) {
		byte[] bytes = MD5.digest("4534569sdhge".getBytes());
		int count = 0;
		long start = System.currentTimeMillis();

		System.out.println("BASE64:" + (System.currentTimeMillis() - start));
		
		long s1 = Bytes.toLong(bytes);
		long s2 = Bytes.toLong(bytes, 8);
		
		for(int i=0;i<10;i++){
			Base62.encode(s1);
			Base62.encode(s2);
			count ++;
		}
		System.out.println("BASE62:" + (System.currentTimeMillis() - start));
		
		String b1 = Base62.encode(s1);
		String b2 = Base62.encode(s2);
		System.out.println(s1 +" - "+b1);
		System.out.println(s2 +" - "+b2);
		base62 = b1+b2;
		System.out.println(base62.length() +":" + base62 );
	}
}

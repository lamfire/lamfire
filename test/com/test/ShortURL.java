package com.test;

import com.lamfire.code.Base62;
import com.lamfire.code.MD5;
import com.lamfire.code.MurmurHash;

public class ShortURL {

	static String md(String text) {
		String hex = MD5.hash(text);
		String subHex = hex.substring(0, 8);
		long result = Long.valueOf("3FFFFFFF", 16) & Long.valueOf(subHex, 16);
		return Base62.encode(result);
	}
	
	static String hash(String text) {
		int result = MurmurHash.hash32(text.getBytes(), 1333);
		return Base62.encode(result);
	}

	public static void main(String[] args) {
		String url = "http://www.lamfire.com/?p=12";
		System.out.println(hash(url));
	}
}

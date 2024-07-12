package com.test;

import com.lamfire.code.Base62;
import com.lamfire.code.MurmurHash;
import com.lamfire.code.PUID;
import com.lamfire.code.Radixes;
import com.lamfire.utils.Asserts;
import com.lamfire.utils.Bytes;
import com.lamfire.utils.RandomUtils;

public class Radix62Test {

	public static void radix62() {
		for(int a=0;a<9999;a++){
			long v = MurmurHash.hash64(Bytes.toBytes(a), 1313);
			System.out.println( v + " -> " +Radixes.digest32(v));
		}
	}
	
	public static void main(String[] args) {
		int radix = 70;
		long number = -999999999;
		String digest = Radixes.encode(number,radix);
		long val = Radixes.decode(digest,radix);
		System.out.println(String.format("[EN] %d - %d -> %s", radix,number,digest));
		System.out.println(String.format("[DE] %d - %s -> %d", radix,digest,val));

		for(int i=0;i<10;i++){
			PUID puid = new PUID();
			String puidBase62 = puid.toBase62();
			String puidRadix62 = Radixes.toBaseXString(puid.toBytes(), 62);
			String puidRadix64 = Radixes.toBaseXString(puid.toBytes(), 70);
			System.out.println(puidBase62  +"           " + puidRadix62 +"           " + puidRadix64) ;
		}




		Asserts.equalsAssert(number,val);
	}

}

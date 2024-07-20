package com.test;

import com.lamfire.code.Hex;
import com.lamfire.code.MurmurHash;
import com.lamfire.code.PUID;
import com.lamfire.code.Radixes;
import com.lamfire.utils.Asserts;
import com.lamfire.utils.Bytes;

public class RadixDecodeTest {

	public static void test_10_70() {
		for(int i=10;i<70;i++) {
			int radix = i;
			long val = 9999999999l;
			String longV = Radixes.encode(val, radix);
			long deVal = Radixes.decode(longV, radix);
			System.out.println("[LONG] : " + val + "   -   " + deVal);

			byte[] longBytes = Bytes.toBytes(val);
			String bytesEn = Radixes.toBaseXString(longBytes, radix);
			byte[] longDe = Radixes.decodeBaseXString(bytesEn, radix);
			System.out.println("[LONG-HEX] : " + Hex.encode(longBytes) + "   -   " + Hex.encode(longDe));
			long toLong = Bytes.toLong(longDe);
			System.out.println("[LONG-VALUE] : " + longV + "   -   " + toLong);

			String s = "1234567890ABCDEFGHIJKL-----1-1----4--------2---你好----555--------666----8111";
			byte[] bytes = s.getBytes();
			System.out.println("[SOURCE] : " + s + " bytes=" + bytes.length + "    radix=" + radix);
			String en = Radixes.toBaseXString(bytes, radix);
			System.out.println("[ENCODE] : " + en);
			byte[] deBytes = Radixes.decodeBaseXString(en, radix);
			System.out.println("[EN-BYTES] : " + Hex.encode(bytes));
			System.out.println("[DE-BYTES] : " + Hex.encode(deBytes));
			String de = new String(deBytes);
			System.out.println("[DECODE] : " + de);

			Asserts.equalsAssert(val, deVal);
			Asserts.equalsAssert(val, toLong);
			Asserts.equalsAssert(s, de);
		}
	}

	public static void test_encode() {

			for (int i = 10; i < 66; i++) {
				int radix = i;
				for (long j = 0; j < Long.MAX_VALUE; j++) {
					long val = j;

					String longV = Radixes.encode(val, radix);
					long deVal = Radixes.decode(longV, radix);

					if( j % 1000000 == 0) {
						System.out.println("[LONG] : " + val + "   -   " + deVal);
					}
					Asserts.equalsAssert(val, deVal);
				}
				System.out.println("felish = " + i);
			}
		}

	public static void test_basex(int radix) {
		String s = "1234567890ABCDEFGHIJKL-----1-1----4--------2---你好----555--------666----8111";
		byte[] bytes = s.getBytes();
		System.out.println("[SOURCE] : " + s + " bytes=" + bytes.length + "    radix=" + radix);
		String en = Radixes.toBaseXString(bytes, radix);
		System.out.println("[ENCODE] : " + en);
		byte[] deBytes = Radixes.decodeBaseXString(en, radix);
		System.out.println("[EN-BYTES] : " + Hex.encode(bytes));
		System.out.println("[DE-BYTES] : " + Hex.encode(deBytes));
		String de = new String(deBytes);
		System.out.println("[DECODE] : " + de);
		Asserts.equalsAssert(s, de);
	}
	public static void main(String[] args) {
		test_10_70();
	}

}

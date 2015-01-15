package com.test;

import com.lamfire.code.AES;

public class AESTest {

	public static void main(String[] args) {
		String ios = "3fb347e2f07b6b4115d2a605c6f00cc43db2dd3825e22a36b42aee551ce5900fe4ed4cdf7cacf7625f819f416b80383be5cd916363a5ec4caec09703f2c53de244a9fb420aa5182388fb704405fe285ba2371a1af32033173f2cc2903cab934f04fda62facb26309e15a4007e5193b8069e104fcb0d377721e5586324fa5570dece01f4a26dc5694579bcb2ec0c11684798d3afb355b715bb51435cd6bc8c1259a573b592bf1a6aa9eb6ca3c7683b0dbcc2ed3299b81db7e787f084b8409d1fcc84289a58c614a226db3a7f05dcff420af3dda9d85df064b742c6b3c46cf317761b525e0001006f91c3617f8c6df9792da2acf8e317482a0a8e6ffb8a06a0f7c2df8db981de74050f5b4aeb1d5ab73e585b10078b1a051f2000a858fe21747762612f76562fca03d2ffdfb7098fcbaf7";
		String source = "1234567890";
		String key = "052E92D80523B70A";
		System.out.println(AES.encode(source, key));
		
		System.out.println(AES.decode(ios, key));
	}
}

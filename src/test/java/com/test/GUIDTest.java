package com.test;

import com.lamfire.code.GUID;

public class GUIDTest {
	static String guid;
	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			guid = GUID.makeAsStandardFormat();
			System.out.println(guid);
		}

	}
}

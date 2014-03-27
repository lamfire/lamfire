package com.test;

import com.lamfire.code.GUIDGen;

public class GUIDTest {
	static String guid;
	public static void main(String[] args) {
		for(int i=0;i<100000;i++){
			guid = GUIDGen.guid();
		}
		System.out.println(guid);
	}
}

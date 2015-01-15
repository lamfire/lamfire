package com.test;

import com.lamfire.utils.RandomUtils;

public class RandomTest {

	public static void main(String[] args) {
		for(int i=0;i<1000;i++){
			System.out.println(RandomUtils.randomTextWithFixedLength(9));
		}
	}
}

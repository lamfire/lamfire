package com.test;

import com.lamfire.utils.Lists;
import com.lamfire.utils.RandomUtils;

import java.util.List;

public class RandomTest {

	public static void randomCollection() {
		List<Integer> list = Lists.newArrayList();
		for (int i = 0; i < 10; i++) {
			list.add(i);
		}
		System.out.println("SIZE : " + list.size());

		RandomUtils.randomCollection(list);

		System.out.println("SIZE : " + list.size());
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}


	}

	public static void randomArray() {
		Integer[] arr = new Integer[10];
		for (int i = 0; i < 10; i++) {
			arr[i] = i;
		}
		System.out.println("SIZE : " + arr.length);

		RandomUtils.randomArray(arr);

		System.out.println("SIZE : " + arr.length);
		for (int i = 0; i < arr.length; i++) {
			System.out.println(arr[i]);
		}


	}

	public static void randomGet(String[] args) {
		List<Integer> list = Lists.newArrayList();
		for (int i = 0; i < 10; i++) {
			list.add(i);
		}

		for (int i = 0; i < list.size(); i++) {
			System.out.println(RandomUtils.randomGet(list));
		}
	}

	public static void main(String[] args) {
		List<Integer> list = Lists.newArrayList();
		for (int i = 0; i < 10; i++) {
			list.add(i);
		}

		RandomUtils.randomList(list);

		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}
}

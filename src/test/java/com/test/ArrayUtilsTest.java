package com.test;

import java.util.ArrayList;
import java.util.List;

import com.lamfire.utils.ArrayUtils;
import com.lamfire.utils.Printers;

public class ArrayUtilsTest {

	public static void main(String[] args) {
		List<Integer> list = new ArrayList<Integer>();
		for(int i=0;i<100;i++){
			list.add(i);
		}
		
		Integer[] array = (Integer[])ArrayUtils.toArray(list,Integer.class);
		Printers.print(array);
		
	}
}

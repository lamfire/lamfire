package com.test;

import com.lamfire.utils.Lists;
import com.lamfire.utils.ObjectUtils;
import com.lamfire.utils.TypeConvertUtils;

import java.util.List;

public class TypeConvertTest {

	static class TypeA{
		private String[] array;
		
		private int intVal;
		
		private float floatVal;
		
		private boolean enable;
		
		private int[] intArray;

		public String[] getArray() {
			return array;
		}

		public void setArray(String[] array) {
			this.array = array;
		}

		public int getIntVal() {
			return intVal;
		}

		public void setIntVal(int intVal) {
			this.intVal = intVal;
		}

		public float getFloatVal() {
			return floatVal;
		}

		public void setFloatVal(float floatVal) {
			this.floatVal = floatVal;
		}

		public int[] getIntArray() {
			return intArray;
		}

		public void setIntArray(int[] intArray) {
			this.intArray = intArray;
		}

		public boolean isEnable() {
			return enable;
		}

		public void setEnable(boolean enable) {
			this.enable = enable;
		}
		
		
		
		
	}
	
	public static void main(String[] args) {
		String [] data = {"123","234","345"};

        List<String> list = Lists.newArrayList();
        list.add("456");
        list.add("567");
        list.add("789");

		Integer [] ints = (Integer [])TypeConvertUtils.toArray(list,Integer.class);
		
		for(int i=0;i<ints.length;i++){
            System.out.println(ints[i]);
        }
		
	}
}

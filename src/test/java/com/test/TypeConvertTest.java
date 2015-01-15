package com.test;

import com.lamfire.utils.ObjectUtils;

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
		TypeA a = new TypeA();
		ObjectUtils.setPropertyValue(a, "intVal", data[0]);
		ObjectUtils.setPropertyValue(a, "enable", '2');
		
		System.out.println(a.isEnable());
		
	}
}

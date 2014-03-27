package com.test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class TEST {

	public static void main(String[] args) throws Exception {
		Collection<String> list = new ArrayList<String>();
		Type[] types = list.getClass().getGenericInterfaces();
		for(Type type : types){
			System.out.println(type.toString());
		}
		Type type = types[0];
		System.out.println(type);
		if(type instanceof ParameterizedType){
			ParameterizedType pt = (ParameterizedType)type;
			System.out.println(pt.getRawType());
			
			System.out.println(List.class.getGenericSuperclass());
		}
	}
}

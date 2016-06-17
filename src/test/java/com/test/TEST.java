package com.test;

import com.lamfire.utils.ClassUtils;
import com.lamfire.utils.IOUtils;
import com.lamfire.utils.StringUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class TEST {

	public static void main(String[] args) throws Exception {
        Byte[] a = new Byte[10];
        Type type = a.getClass();
        System.out.println(type);

        a = (Byte[])ClassUtils.newComponentTypeArray(Byte.class, 10);
        System.out.println("instance : " + a);
	}
}

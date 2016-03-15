package com.test;

import com.lamfire.utils.IOUtils;
import com.lamfire.utils.StringUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class TEST {

	public static void main(String[] args) throws Exception {
        Exception e = new RuntimeException("aaaaaaaaaaaa");
        System.out.println(IOUtils.getStackTraceAsStringBuffer(e));
	}
}

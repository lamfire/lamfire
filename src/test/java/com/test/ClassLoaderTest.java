package com.test;

import com.lamfire.utils.ClassLoaderUtils;
import com.lamfire.utils.Printers;

import java.io.IOException;
import java.util.Set;

public class ClassLoaderTest {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Set<Class<?>> classes = ClassLoaderUtils.getClasses("com.lamfire");
		Printers.print(classes);
	}
}

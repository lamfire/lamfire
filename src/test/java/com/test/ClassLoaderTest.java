package com.test;

import java.io.IOException;
import java.util.Set;

import com.lamfire.utils.ClassLoaderUtils;
import com.lamfire.utils.Printers;

public class ClassLoaderTest {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Set<Class<?>> classes = ClassLoaderUtils.getClasses("org.slf4j");
		Printers.print(classes);
	}
}

package com.lamfire.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

import com.lamfire.anno.PROP_BOUND;
import com.lamfire.logger.Logger;

public class PropertiesUtils {

	private static final Logger LOGGER = Logger.getLogger(PropertiesUtils.class.getName());
	private static final Map<String,Properties> caches = new HashMap<>();

	public static Map<String, String> loadAsMap(InputStream input) {
		if (input == null) {
			throw new IllegalArgumentException("input stream is null.");
		}
		return toMap(load(input));
	}

	public static Properties load(InputStream input) {
		if (input == null) {
			throw new IllegalArgumentException("input stream is null.");
		}
		Properties properties = new Properties();
		try {
			properties.load(input);
		} catch (Exception e) {
			LOGGER.warn( e.getMessage(), e);
			return null;
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				LOGGER.warn(e.getMessage(), e);
			}
		}
		return properties;
	}

	public static Properties load(String resource, Class<?> callingClass) {
		File file = ClassLoaderUtils.getResourceAsFile(resource, callingClass);
		String cacheKey = file.getAbsolutePath();
		if(caches.containsKey(cacheKey)){
			return caches.get(caches);
		}
		return load(file);
	}

	public static Properties load(File file) {
		String cacheKey = file.getAbsolutePath();
		if(caches.containsKey(cacheKey)){
			return caches.get(caches);
		}
		try {
			Properties prop =  load(new FileInputStream(file));
			caches.put(cacheKey,prop);
			return prop;
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException(e.getMessage(), e);
		}
	}

	public static Map<String, String> loadAsMap(File file) {
		return toMap(load(file));
	}

	public static Map<String, String> loadAsMap(String resource,Class<?> callingClass) {
		return toMap(load(resource,callingClass));
	}

	public static Map<String, String> toMap(Properties properties) {
		Map<String, String> map = new HashMap<String, String>();
		if (properties == null) {
			return map;
		}
		Enumeration<?> e = properties.propertyNames();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			String val = properties.getProperty(key.toString(), "");
			map.put(key, val);
		}
		return map;
	}

	public static void boundByAnnotation(Object instance){
		Set<Field> fields = AnnotationUtils.getAnnotationFields(instance.getClass(), PROP_BOUND.class);
		for(Field f : fields){
			PROP_BOUND b  = f.getAnnotation(PROP_BOUND.class);
			String prop = b.prop();
			String key = b.key();
			Properties p = load(prop , instance.getClass());
			ObjectUtils.setFieldValue(instance,f,p.get(key));
		}
	}
}

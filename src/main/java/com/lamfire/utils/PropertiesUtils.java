package com.lamfire.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
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
		return load(file);
	}

	public static void loadToObjectFields(String resource,Object instance){
		Properties properties = load(resource,instance.getClass());
		Field[] fields = ClassUtils.getAllFields(instance.getClass());
		for(Field field : fields){
			String name = field.getName();
			String value = properties.getProperty(name);
			Class<?> type = field.getType();
			if(StringUtils.isBlank(value)){
				continue;
			}
			try {
				field.setAccessible(true);
				field.set(instance, TypeConvertUtils.convert(value, type));
			}catch (IllegalAccessException e) {
				LOGGER.error("Set field value field : " +instance.getClass().getName() +"."+ field.getName(),e);
			}finally {
				field.setAccessible(false);
			}
		}
	}

	/**
	 * 加载资源文件到一个静态fields
	 */
	public static void loadToStaticFields(String resource, Class<?> classes) {
			Properties properties = load(resource,classes);
			Field[] fields = classes.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				String value = properties.getProperty(field.getName());
				if (StringUtils.isBlank(value)) {
					continue;
				}
				try {
					Class<?> type = field.getType();
					field.set(null,TypeConvertUtils.convert(value,type));
				} catch (IllegalAccessException e) {
					LOGGER.error("Not a static field : " +classes.getName() +"."+ field.getName());
				}
			}
	}

	public static Properties load(File file) {
		String cacheKey = file.getAbsolutePath();
		if(caches.containsKey(cacheKey)){
			return caches.get(cacheKey);
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

	public static void loadByAnnotation(Object instance){
		Set<Field> fields = AnnotationUtils.getAnnotationFields(instance.getClass(), PROP_BOUND.class);
		for(Field f : fields){
			PROP_BOUND b = f.getAnnotation(PROP_BOUND.class);
			if(b == null){
				continue;
			}
			String prop = b.prop();
			String key = b.key();
			Properties p = load(prop, instance.getClass());
			if(p == null){
				continue;
			}
			String value = p.getProperty(key);
			try {
				ObjectUtils.setPropertyValue(instance, f.getName(), value);
			}catch (Exception e){
				LOGGER.warn(instance.getClass().getName() + "." + f.getName() + " set value : " + value +" error." + e.getMessage(),e );
			}
		}
	}
}

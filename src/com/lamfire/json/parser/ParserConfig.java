package com.lamfire.json.parser;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.regex.Pattern;

import com.lamfire.json.JSONArray;
import com.lamfire.json.JSONException;
import com.lamfire.json.JSON;
import com.lamfire.json.deserializer.ASMDeserializerFactory;
import com.lamfire.json.deserializer.ASMJavaBeanDeserializer;
import com.lamfire.json.deserializer.ArrayDeserializer;
import com.lamfire.json.deserializer.ArrayListStringDeserializer;
import com.lamfire.json.deserializer.ArrayListStringFieldDeserializer;
import com.lamfire.json.deserializer.ArrayListTypeDeserializer;
import com.lamfire.json.deserializer.ArrayListTypeFieldDeserializer;
import com.lamfire.json.deserializer.AtomicIntegerArrayDeserializer;
import com.lamfire.json.deserializer.AtomicLongArrayDeserializer;
import com.lamfire.json.deserializer.AutowiredObjectDeserializer;
import com.lamfire.json.deserializer.BigDecimalDeserializer;
import com.lamfire.json.deserializer.BigIntegerDeserializer;
import com.lamfire.json.deserializer.BooleanDeserializer;
import com.lamfire.json.deserializer.BooleanFieldDeserializer;
import com.lamfire.json.deserializer.ByteDeserializer;
import com.lamfire.json.deserializer.CharacterDeserializer;
import com.lamfire.json.deserializer.CharsetDeserializer;
import com.lamfire.json.deserializer.CollectionDeserializer;
import com.lamfire.json.deserializer.ConcurrentHashMapDeserializer;
import com.lamfire.json.deserializer.DateDeserializer;
import com.lamfire.json.deserializer.DefaultFieldDeserializer;
import com.lamfire.json.deserializer.DefaultObjectDeserializer;
import com.lamfire.json.deserializer.DoubleDeserializer;
import com.lamfire.json.deserializer.EnumDeserializer;
import com.lamfire.json.deserializer.FieldDeserializer;
import com.lamfire.json.deserializer.FileDeserializer;
import com.lamfire.json.deserializer.FloatDeserializer;
import com.lamfire.json.deserializer.HashMapDeserializer;
import com.lamfire.json.deserializer.InetAddressDeserializer;
import com.lamfire.json.deserializer.InetSocketAddressDeserializer;
import com.lamfire.json.deserializer.IntegerDeserializer;
import com.lamfire.json.deserializer.IntegerFieldDeserializer;
import com.lamfire.json.deserializer.JSONArrayDeserializer;
import com.lamfire.json.deserializer.JSONObjectDeserializer;
import com.lamfire.json.deserializer.JavaBeanDeserializer;
import com.lamfire.json.deserializer.JavaObjectDeserializer;
import com.lamfire.json.deserializer.LinkedHashMapDeserializer;
import com.lamfire.json.deserializer.LocaleDeserializer;
import com.lamfire.json.deserializer.LongDeserializer;
import com.lamfire.json.deserializer.LongFieldDeserializer;
import com.lamfire.json.deserializer.NumberDeserializer;
import com.lamfire.json.deserializer.ObjectDeserializer;
import com.lamfire.json.deserializer.PatternDeserializer;
import com.lamfire.json.deserializer.ShortDeserializer;
import com.lamfire.json.deserializer.SqlDateDeserializer;
import com.lamfire.json.deserializer.StringDeserializer;
import com.lamfire.json.deserializer.StringFieldDeserializer;
import com.lamfire.json.deserializer.ThrowableDeserializer;
import com.lamfire.json.deserializer.TimeZoneDeserializer;
import com.lamfire.json.deserializer.TimestampDeserializer;
import com.lamfire.json.deserializer.TreeMapDeserializer;
import com.lamfire.json.deserializer.URIDeserializer;
import com.lamfire.json.deserializer.URLDeserializer;
import com.lamfire.json.deserializer.UUIDDeserializer;
import com.lamfire.json.util.ASMUtils;
import com.lamfire.json.util.FieldInfo;
import com.lamfire.json.util.IdentityHashMap;
import com.lamfire.json.util.ServiceLoader;
import com.lamfire.json.util.SymbolTable;

public class ParserConfig {

	public static ParserConfig getGlobalInstance() {
		return global;
	}

	private final Set<Class<?>> primitiveClasses = new HashSet<Class<?>>();

	private static ParserConfig global = new ParserConfig();

	private final IdentityHashMap<Type, ObjectDeserializer> derializers = new IdentityHashMap<Type, ObjectDeserializer>();

	private DefaultObjectDeserializer defaultSerializer = new DefaultObjectDeserializer();

	private boolean asmEnable = !ASMUtils.isAndroid();

	protected final SymbolTable symbolTable = new SymbolTable();

	public DefaultObjectDeserializer getDefaultSerializer() {
		return defaultSerializer;
	}

	public ParserConfig() {
		primitiveClasses.add(boolean.class);
		primitiveClasses.add(Boolean.class);

		primitiveClasses.add(char.class);
		primitiveClasses.add(Character.class);

		primitiveClasses.add(byte.class);
		primitiveClasses.add(Byte.class);

		primitiveClasses.add(short.class);
		primitiveClasses.add(Short.class);

		primitiveClasses.add(int.class);
		primitiveClasses.add(Integer.class);

		primitiveClasses.add(long.class);
		primitiveClasses.add(Long.class);

		primitiveClasses.add(float.class);
		primitiveClasses.add(Float.class);

		primitiveClasses.add(double.class);
		primitiveClasses.add(Double.class);

		primitiveClasses.add(BigInteger.class);
		primitiveClasses.add(BigDecimal.class);

		primitiveClasses.add(String.class);
		primitiveClasses.add(java.util.Date.class);
		primitiveClasses.add(java.sql.Date.class);
		primitiveClasses.add(java.sql.Time.class);
		primitiveClasses.add(java.sql.Timestamp.class);

		derializers.put(java.sql.Timestamp.class, TimestampDeserializer.instance);
		derializers.put(java.sql.Date.class, SqlDateDeserializer.instance);
		derializers.put(java.util.Date.class, DateDeserializer.instance);

		derializers.put(JSON.class, JSONObjectDeserializer.instance);
		derializers.put(JSONArray.class, JSONArrayDeserializer.instance);
		
		derializers.put(Map.class, HashMapDeserializer.instance);
		derializers.put(HashMap.class, HashMapDeserializer.instance);
		derializers.put(LinkedHashMap.class, LinkedHashMapDeserializer.instance);
		derializers.put(TreeMap.class, TreeMapDeserializer.instance);
		derializers.put(ConcurrentMap.class, ConcurrentHashMapDeserializer.instance);
		derializers.put(ConcurrentHashMap.class, ConcurrentHashMapDeserializer.instance);

		derializers.put(Collection.class, CollectionDeserializer.instance);
		derializers.put(List.class, CollectionDeserializer.instance);
		derializers.put(ArrayList.class, CollectionDeserializer.instance);

		derializers.put(Object.class, JavaObjectDeserializer.instance);
		derializers.put(String.class, StringDeserializer.instance);
		derializers.put(char.class, CharacterDeserializer.instance);
		derializers.put(Character.class, CharacterDeserializer.instance);
		derializers.put(byte.class, ByteDeserializer.instance);
		derializers.put(Byte.class, ByteDeserializer.instance);
		derializers.put(short.class, ShortDeserializer.instance);
		derializers.put(Short.class, ShortDeserializer.instance);
		derializers.put(int.class, IntegerDeserializer.instance);
		derializers.put(Integer.class, IntegerDeserializer.instance);
		derializers.put(long.class, LongDeserializer.instance);
		derializers.put(Long.class, LongDeserializer.instance);
		derializers.put(BigInteger.class, BigIntegerDeserializer.instance);
		derializers.put(BigDecimal.class, BigDecimalDeserializer.instance);
		derializers.put(float.class, FloatDeserializer.instance);
		derializers.put(Float.class, FloatDeserializer.instance);
		derializers.put(double.class, DoubleDeserializer.instance);
		derializers.put(Double.class, DoubleDeserializer.instance);
		derializers.put(boolean.class, BooleanDeserializer.instance);
		derializers.put(Boolean.class, BooleanDeserializer.instance);

		derializers.put(UUID.class, UUIDDeserializer.instance);
		derializers.put(TimeZone.class, TimeZoneDeserializer.instance);
		derializers.put(Locale.class, LocaleDeserializer.instance);
		derializers.put(InetAddress.class, InetAddressDeserializer.instance);
		derializers.put(Inet4Address.class, InetAddressDeserializer.instance);
		derializers.put(Inet6Address.class, InetAddressDeserializer.instance);
		derializers.put(InetSocketAddress.class, InetSocketAddressDeserializer.instance);
		derializers.put(File.class, FileDeserializer.instance);
		derializers.put(URI.class, URIDeserializer.instance);
		derializers.put(URL.class, URLDeserializer.instance);
		derializers.put(Pattern.class, PatternDeserializer.instance);
		derializers.put(Charset.class, CharsetDeserializer.instance);
		derializers.put(Number.class, NumberDeserializer.instance);
		derializers.put(AtomicIntegerArray.class, AtomicIntegerArrayDeserializer.instance);
		derializers.put(AtomicLongArray.class, AtomicLongArrayDeserializer.instance);

	}

	public boolean isAsmEnable() {
		return asmEnable;
	}

	public void setAsmEnable(boolean asmEnable) {
		this.asmEnable = asmEnable;
	}

	public SymbolTable getSymbolTable() {
		return symbolTable;
	}

	public IdentityHashMap<Type, ObjectDeserializer> getDerializers() {
		return derializers;
	}

	public ObjectDeserializer getDeserializer(Type type) {
		ObjectDeserializer derializer = this.derializers.get(type);
		if (derializer != null) {
			return derializer;
		}

		if (type instanceof Class<?>) {
			return getDeserializer((Class<?>) type, type);
		}

		if (type instanceof ParameterizedType) {
			Type rawType = ((ParameterizedType) type).getRawType();
			return getDeserializer((Class<?>) rawType, type);
		}
		
		if(type instanceof GenericArrayType){
			return ArrayDeserializer.instance;
		}
		

		return this.defaultSerializer;
	}

	public ObjectDeserializer getDeserializer(Class<?> clazz, Type type) {
		ObjectDeserializer derializer = derializers.get(type);
		if (derializer != null) {
			return derializer;
		}

		final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		for (AutowiredObjectDeserializer autowired : ServiceLoader.load(AutowiredObjectDeserializer.class, classLoader)) {
			for (Type forType : autowired.getAutowiredFor()) {
				derializers.put(forType, autowired);
			}
		}

		derializer = derializers.get(type);
		if (derializer != null) {
			return derializer;
		}

		if (clazz.isEnum()) {
			derializer = new EnumDeserializer(clazz);
		} else if (clazz.isArray()) {
			return ArrayDeserializer.instance;
		} else if (clazz == Set.class || clazz == Collection.class || clazz == List.class || clazz == ArrayList.class) {
			if (type instanceof ParameterizedType) {
				Type itemType = ((ParameterizedType) type).getActualTypeArguments()[0];
				if (itemType == String.class) {
					derializer = ArrayListStringDeserializer.instance;
				} else {
					derializer = new ArrayListTypeDeserializer(itemType);
				}
			} else {
				derializer = CollectionDeserializer.instance;
			}
		} else if (Collection.class.isAssignableFrom(clazz)) {
			derializer = CollectionDeserializer.instance;
		} else if (Map.class.isAssignableFrom(clazz)) {
			derializer = this.defaultSerializer;
		} else if (Throwable.class.isAssignableFrom(clazz)) {
			derializer = new ThrowableDeserializer(this, clazz);
		} else {
			derializer = createJavaBeanDeserializer(clazz);
		}

		putDeserializer(type, derializer);

		return derializer;
	}

	public ObjectDeserializer createJavaBeanDeserializer(Class<?> clazz) {
		if (clazz == Class.class) {
			return this.defaultSerializer;
		}

		if (!Modifier.isPublic(clazz.getModifiers())) {
			return new JavaBeanDeserializer(this, clazz);
		}

		if (!asmEnable) {
			return new JavaBeanDeserializer(this, clazz);
		}

		try {
			return ASMDeserializerFactory.getInstance().createJavaBeanDeserializer(this, clazz);
		} catch (Exception e) {
			throw new JSONException("create asm deserializer error, " + clazz.getName(), e);
		}
	}

	public FieldDeserializer createFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo) {
		boolean asmEnable = this.asmEnable;

		Method method = fieldInfo.getMethod();

		if (!Modifier.isPublic(clazz.getModifiers())) {
			asmEnable = false;
		}

		if (method.getParameterTypes()[0] == Class.class) {
			asmEnable = false;
		}

		if (!asmEnable) {
			return createFieldDeserializerWithoutASM(mapping, clazz, fieldInfo);
		}

		try {
			return ASMDeserializerFactory.getInstance().createFieldDeserializer(mapping, clazz, fieldInfo);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return createFieldDeserializerWithoutASM(mapping, clazz, fieldInfo);
	}

	public FieldDeserializer createFieldDeserializerWithoutASM(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo) {
		Method method = fieldInfo.getMethod();
		Class<?> fieldClass = method.getParameterTypes()[0];

		if (fieldClass == boolean.class || fieldClass == Boolean.class) {
			return new BooleanFieldDeserializer(mapping, clazz, fieldInfo);
		}

		if (fieldClass == int.class || fieldClass == Integer.class) {
			return new IntegerFieldDeserializer(mapping, clazz, fieldInfo);
		}

		if (fieldClass == long.class || fieldClass == Long.class) {
			return new LongFieldDeserializer(mapping, clazz, fieldInfo);
		}

		if (fieldClass == String.class) {
			return new StringFieldDeserializer(mapping, clazz, fieldInfo);
		}

		if (fieldClass == List.class || fieldClass == ArrayList.class) {
			Type fieldType = method.getGenericParameterTypes()[0];
			Type itemType = ((ParameterizedType) fieldType).getActualTypeArguments()[0];
			if (itemType == String.class) {
				return new ArrayListStringFieldDeserializer(mapping, clazz, fieldInfo);
			}

			return new ArrayListTypeFieldDeserializer(mapping, clazz, fieldInfo);
		}

		return new DefaultFieldDeserializer(mapping, clazz, fieldInfo);
	}

	public void putDeserializer(Type type, ObjectDeserializer deserializer) {
		derializers.put(type, deserializer);
	}

	public ObjectDeserializer getDeserializer(FieldInfo fieldInfo) {
		return getDeserializer(fieldInfo.getFieldClass(), fieldInfo.getFieldType());
	}

	public boolean isPrimitive(Class<?> clazz) {
		return primitiveClasses.contains(clazz);
	}

	public static Field getField(Class<?> clazz, String fieldName) {
		try {
			return clazz.getDeclaredField(fieldName);
		} catch (Exception e) {
			return null;
		}
	}

	public Map<String, FieldDeserializer> getFieldDeserializers(Class<?> clazz) {
		ObjectDeserializer deserizer = getDeserializer(clazz);

		if (deserizer instanceof JavaBeanDeserializer) {
			return ((JavaBeanDeserializer) deserizer).getFieldDeserializerMap();
		} else if (deserizer instanceof ASMJavaBeanDeserializer) {
			return ((ASMJavaBeanDeserializer) deserizer).getInnterSerializer().getFieldDeserializerMap();
		} else {
			return Collections.emptyMap();
		}
	}

}

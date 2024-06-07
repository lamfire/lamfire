package com.lamfire.utils;

import com.lamfire.code.Yarrow;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class RandomUtils {
	private static final String STRINGS = "1234567890poiuytrewqasdfghjklmnbvcxzQWERTYUIOPLKJHGFDSAZXCVBNM";
	public static final Random RANDOM = new Random(System.currentTimeMillis());
	public static final Random yarrow = new Yarrow();

	public static int nextInt() {
		return nextInt(RANDOM);
	}

	public static int nextInt(Random random) {
		return random.nextInt();
	}

	public static int nextInt(int n) {
		return nextInt(RANDOM, n);
	}

	public static int nextInt(Random random, int n) {
		return random.nextInt(n);
	}

	public static long nextLong() {
		return nextLong(RANDOM);
	}

	public static long nextLong(long n) {
		return nextLong(RANDOM,n);
	}

	public static long nextLong(Random random) {
		return random.nextLong();
	}

	public static long nextLong(Random random,long n) {
		return random.nextLong(n);
	}

	public static boolean nextBoolean() {
		return nextBoolean(RANDOM);
	}

	public static boolean nextBoolean(Random random) {
		return random.nextBoolean();
	}

	public static float nextFloat() {
		return nextFloat(RANDOM);
	}

	public static float nextFloat(float n) {
		return nextFloat(RANDOM,n);
	}

	public static float nextFloat(Random random) {
		return random.nextFloat();
	}

	public static float nextFloat(Random random,float n) {
		return random.nextFloat(n);
	}

	public static double nextDouble() {
		return nextDouble(RANDOM);
	}

	public static double nextDouble(double n) {
		return nextDouble(RANDOM,n);
	}

	public static double nextDouble(Random random) {
		return random.nextDouble();
	}

	public static double nextDouble(Random random,double n) {
		return random.nextDouble(n);
	}

	public static String random(int bits, int radix) {
		return new BigInteger(bits, yarrow).toString(radix).toUpperCase();
	}

	public static String randomMACAddr() {
		return String.format("%02x:%02x:%02x:%02x:%02x:%02x", RandomUtils.nextInt(255), RandomUtils.nextInt(255), RandomUtils.nextInt(255), RandomUtils.nextInt(255), RandomUtils
				.nextInt(255), RandomUtils.nextInt(255));
	}

	public static String randomIPAddr() {
		return String.format("%d.%d.%d.%d", RandomUtils.nextInt(230), RandomUtils.nextInt(255), RandomUtils.nextInt(255), RandomUtils.nextInt(255));
	}
	
	public static String randomText(int maxLength) {
		int size = nextInt(maxLength +1);
		char[] chars = new char[size];
		for(int i=0;i<size;i++){
			chars[i] = STRINGS.charAt(nextInt(STRINGS.length()));
		}
		return String.valueOf(chars);
	}
	
	public static String randomText(int minLength ,int maxLength) {
		int size = minLength + nextInt(maxLength - minLength +1);
		char[] chars = new char[size];
		for(int i=0;i<size;i++){
			chars[i] = STRINGS.charAt(nextInt(STRINGS.length()));
		}
		return String.valueOf(chars);
	}

	public static String randomTextWithFixedLength(int size) {
		char[] chars = new char[size];
		for(int i=0;i<size;i++){
			chars[i] = STRINGS.charAt(nextInt(STRINGS.length()));
		}
		return String.valueOf(chars);
	}

	public static <T> void randomList(List<T> list) {
		int len = list.size();
		for (int i = 0; i < len; i++) {
			T e = list.get(i);
			int r = nextInt(len);
			if (r == i) {
				continue;
			}
			T o = list.get(r);
			list.set(i, o);
			list.set(r, e);
		}
	}


	public static <T> void randomCollection(Collection<T> list) {
		List<T> result = Lists.newArrayList(list);
		int len = result.size();
		for (int i = 0; i < len; i++) {
			T e = result.get(i);
			int r = nextInt(len);
			if (r == i) {
				continue;
			}
			T o = result.get(r);
			result.set(i, o);
			result.set(r, e);
		}
		list.clear();
		list.addAll(result);
		result.clear();
	}

	public static void randomArray(Object[] arr) {
		int len = arr.length;
		for (int i = 0; i < len; i++) {
			Object e = arr[i];
			int r = nextInt(len);
			if (r == i) {
				continue;
			}
			Object o = arr[r];
			arr[i] = o;
			arr[r] = e;
		}
	}

	public static <T> T randomGet(List<T> list) {
		if (list == null && list.isEmpty()) {
			return null;
		}
		return list.get(nextInt(list.size()));
	}

	public static <T> T randomGet(Collection<T> col) {
		if (col == null && col.isEmpty()) {
			return null;
		}
		T result = null;
		Iterator<T> iterator = col.iterator();
		int r = nextInt(col.size());

		for (int i = 0; i < r; i++) {
			result = iterator.next();
		}
		return result;
	}

	public static double scale(double source,int dots){
		return new BigDecimal(source).setScale(dots, RoundingMode.DOWN).doubleValue();
	}

	public static float scale(float source,int dots){
		return new BigDecimal(source).setScale(dots, RoundingMode.DOWN).floatValue();
	}

	public static float randomFloat(float min,float max,int dots){
		float val = min + RANDOM.nextFloat(max - min);
		return scale(val,dots);
	}

	public static double randomDouble(double min,double max,int dots){
		double val = min + RANDOM.nextDouble(max - min);
		return scale(val,dots);
	}
}

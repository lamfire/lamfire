package com.lamfire.utils;

import java.lang.reflect.Array;

public class Asserts {
	public static void assertTrue(String message, boolean condition) {
		if (!condition)
			fail(message);
	}

	public static void assertTrue(boolean condition) {
		assertTrue(null, condition);
	}

	public static void assertFalse(String message, boolean condition) {
		assertTrue(message, !condition);
	}

	public static void assertFalse(boolean condition) {
		assertFalse(null, condition);
	}

	public static void fail(String message) {
		throw new AssertionError(message == null ? "" : message);
	}

	public static void fail() {
		fail(null);
	}

	public static void assertEquals(String message, Object expected, Object actual) {
		if ((expected == null) && (actual == null))
			return;
		if ((expected != null) && (isEquals(expected, actual)))
			return;
		if (((expected instanceof String)) && ((actual instanceof String))) {
			String cleanMessage = message == null ? "" : message;
			throw new RuntimeException(cleanMessage + "_" + expected + "_" + actual);
		}

		failNotEquals(message, expected, actual);
	}

	private static boolean isEquals(Object expected, Object actual) {
		return expected.equals(actual);
	}

	public static void assertEquals(Object expected, Object actual) {
		assertEquals(null, expected, actual);
	}

	public static void assertArrayEquals(String message, Object[] expecteds, Object[] actuals)

	{
		internalArrayEquals(message, expecteds, actuals);
	}

	public static void assertArrayEquals(Object[] expecteds, Object[] actuals) {
		assertArrayEquals(null, expecteds, actuals);
	}

	public static void assertArrayEquals(String message, byte[] expecteds, byte[] actuals)

	{
		internalArrayEquals(message, expecteds, actuals);
	}

	public static void assertArrayEquals(byte[] expecteds, byte[] actuals) {
		assertArrayEquals(null, expecteds, actuals);
	}

	public static void assertArrayEquals(String message, char[] expecteds, char[] actuals)

	{
		internalArrayEquals(message, expecteds, actuals);
	}

	public static void assertArrayEquals(char[] expecteds, char[] actuals) {
		assertArrayEquals(null, expecteds, actuals);
	}

	public static void assertArrayEquals(String message, short[] expecteds, short[] actuals)

	{
		internalArrayEquals(message, expecteds, actuals);
	}

	public static void assertArrayEquals(short[] expecteds, short[] actuals) {
		assertArrayEquals(null, expecteds, actuals);
	}

	public static void assertArrayEquals(String message, int[] expecteds, int[] actuals)

	{
		internalArrayEquals(message, expecteds, actuals);
	}

	public static void assertArrayEquals(int[] expecteds, int[] actuals) {
		assertArrayEquals(null, expecteds, actuals);
	}

	public static void assertArrayEquals(String message, long[] expecteds, long[] actuals)

	{
		internalArrayEquals(message, expecteds, actuals);
	}

	public static void assertArrayEquals(long[] expecteds, long[] actuals) {
		assertArrayEquals(null, expecteds, actuals);
	}

	private static void internalArrayEquals(String message, Object expecteds, Object actuals)

	{
		if (expecteds == actuals)
			return;
		String header = message + ": ";
		if (expecteds == null)
			fail(header + "expected array was null");
		if (actuals == null)
			fail(header + "actual array was null");
		int actualsLength = Array.getLength(actuals);
		int expectedsLength = Array.getLength(expecteds);
		if (actualsLength != expectedsLength) {
			fail(header + "array lengths differed, expected.length=" + expectedsLength + " actual.length=" + actualsLength);
		}

		for (int i = 0; i < expectedsLength; i++) {
			Object expected = Array.get(expecteds, i);
			Object actual = Array.get(actuals, i);
			if ((isArray(expected)) && (isArray(actual)))
				internalArrayEquals(message, expected, actual);

			else
				assertEquals(expected, actual);
		}
	}

	private static boolean isArray(Object expected) {
		return (expected != null) && (expected.getClass().isArray());
	}

	public static void assertEquals(String message, double expected, double actual, double delta) {
		if (Double.compare(expected, actual) == 0)
			return;
		if (Math.abs(expected - actual) > delta)
			failNotEquals(message, new Double(expected), new Double(actual));
	}

	public static void assertEquals(long expected, long actual) {
		assertEquals(null, expected, actual);
	}

	public static void assertEquals(String message, long expected, long actual) {
		assertEquals(message, Long.valueOf(expected), Long.valueOf(actual));
	}

	
	public static void assertEquals(double expected, double actual) {
		assertEquals(null, expected, actual);
	}


	public static void assertEquals(String message, double expected, double actual) {
		fail("Use assertEquals(expected, actual, delta) to compare floating-point numbers");
	}

	public static void assertEquals(double expected, double actual, double delta) {
		assertEquals(null, expected, actual, delta);
	}

	public static void assertNotNull(String message, Object object) {
		assertTrue(message, object != null);
	}

	public static void assertNotNull(Object object) {
		assertNotNull(null, object);
	}

	public static void assertNull(String message, Object object) {
		assertTrue(message, object == null);
	}

	public static void assertNull(Object object) {
		assertNull(null, object);
	}

	public static void assertSame(String message, Object expected, Object actual) {
		if (expected == actual)
			return;
		failNotSame(message, expected, actual);
	}

	public static void assertSame(Object expected, Object actual) {
		assertSame(null, expected, actual);
	}

	public static void assertNotSame(String message, Object unexpected, Object actual) {
		if (unexpected == actual)
			failSame(message);
	}

	public static void assertNotSame(Object unexpected, Object actual) {
		assertNotSame(null, unexpected, actual);
	}

	private static void failSame(String message) {
		String formatted = "";
		if (message != null)
			formatted = message + " ";
		fail(formatted + "expected not same");
	}

	private static void failNotSame(String message, Object expected, Object actual) {
		String formatted = "";
		if (message != null)
			formatted = message + " ";
		fail(formatted + "expected same:<" + expected + "> was not:<" + actual + ">");
	}

	private static void failNotEquals(String message, Object expected, Object actual) {
		fail(format(message, expected, actual));
	}

	static String format(String message, Object expected, Object actual) {
		String formatted = "";
		if ((message != null) && (!message.equals("")))
			formatted = message + " ";
		String expectedString = String.valueOf(expected);
		String actualString = String.valueOf(actual);
		if (expectedString.equals(actualString)) {
			return formatted + "expected: " + formatClassAndValue(expected, expectedString) + " but was: " + formatClassAndValue(actual, actualString);
		}

		return formatted + "expected:<" + expectedString + "> but was:<" + actualString + ">";
	}

	private static String formatClassAndValue(Object value, String valueString) {
		String className = value == null ? "null" : value.getClass().getName();
		return className + "<" + valueString + ">";
	}

	public static void assertEquals(String message, Object[] expecteds, Object[] actuals) {
		assertArrayEquals(message, expecteds, actuals);
	}

	public static void assertEquals(Object[] expecteds, Object[] actuals) {
		assertArrayEquals(expecteds, actuals);
	}
}
package com.test;

import com.lamfire.logger.Logger;

public class LoggerTest {
	static Logger LOGGER = Logger.getLogger(LoggerTest.class);

	public static void main(String[] args) {
		LOGGER.info(LOGGER.getClass().getName());
	}
}

package com.test;

import com.lamfire.utils.Bytes;
import com.lamfire.utils.ZipUtils;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class ZipTest {

	public static void main(String[] args) throws Exception {
		byte[] source = "1333333333333333333333333333你好1122444123123123".getBytes("utf-8");
		byte[] zip = ZipUtils.zip(source);

        System.out.println(ZipUtils.isZip(zip));

	}

}

package com.test;

import com.lamfire.utils.ZipUtils;

public class ZipTest {

	public static void main(String[] args) throws Exception {
		byte[] source = "{availableProcessors:4,garbageCollectors:[{collectionCount:3,collectionTime:4,name:Copy}".getBytes("utf-8");
		byte[] gzip = ZipUtils.gzip(source);
		byte[] ungzip = ZipUtils.ungzip(gzip);
		
		byte[] zip = ZipUtils.zip(source);
		byte[] unzip = ZipUtils.unzip(zip);
		System.out.println("src:\t"+source.length);
		System.out.println("gzip:\t"+gzip.length);
		System.out.println("ungzip:\t"+ungzip.length);
		System.out.println("zip:\t"+zip.length);
		System.out.println("unzip:\t"+unzip.length);
	}

}

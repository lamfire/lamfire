package com.test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.lamfire.utils.ClassLoaderUtils;
import com.lamfire.utils.FileUtils;
import com.lamfire.utils.IOUtils;
import com.lamfire.utils.StringUtils;

public class StringUtilsTest {
	
	public static List<String> toLines(String source){
		List<String> list = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new StringReader(source));
		try{
			while(true){
				String line = reader.readLine();
				if(line == null){
					break;
				}
				list.add(line);
			}
		}catch(Exception e){
			
		}finally{
			IOUtils.closeQuietly(reader);
		}
		return list;
	}

	public static void toLine() throws Exception{
		byte[] data = FileUtils.readFileToByteArray(ClassLoaderUtils.getResourceAsFile("demodata_linux.log", StringUtilsTest.class));
		String s = new String(data);
		List<String> list = toLines(s);
		for(String line : list){
			System.out.println(line);
		}
	}
	
	public static void main(String[] args) {
		String source = "[RUN]:1354569845632|516405ba7d17e0e755f32713|a23efb45d|cn.sharesdk|1.2.3|18|1";
		String start = "[RUN]";
		String end = "18|10";
		System.out.println(StringUtils.isStartWith(source, start));
		System.out.println(StringUtils.isEndWith(source, end));
	}
}

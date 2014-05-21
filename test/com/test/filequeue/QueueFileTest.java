package com.test.filequeue;

import java.io.File;

import com.lamfire.filequeue.FileQueue;
import com.lamfire.utils.Asserts;



/**
 * Tests for QueueFile.
 * 
 * @author Bob Lee (bob@squareup.com)
 */
public class QueueFileTest  {
	public static void main(String[] args) throws Exception {
		String source = "12345";
		FileQueue queue = new FileQueue("/data/FileQueue/" ,"test1");
		queue.add(source.getBytes());
		queue.close();
		
		queue = new FileQueue("/data/FileQueue/" ,"test1");
		byte[] bytes = queue.peek();
		String value = new String(bytes);
		System.out.println(value);
		Asserts.assertEquals(source, value);
		
		System.out.println(queue.size());
	}
}

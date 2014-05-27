package com.test.filequeue;

import com.lamfire.filequeue.FileBuffer;
import com.lamfire.filequeue.FileQueue;
import com.lamfire.utils.Asserts;
import com.lamfire.utils.Threads;

import java.io.File;


/**
 * Tests for QueueFile.
 * 
 * @author Bob Lee (bob@squareup.com)
 */
public class FileBufferTest {
	public static void main(String[] args) throws Exception {
        FileBuffer buffer = new FileBuffer(new File("/data/FileQueue/Buffer.buf"));

        String sou = "12345";
        byte[] bytes = sou.getBytes();
        buffer.put(bytes);
        buffer.get(bytes);

        String s= new String(bytes);
        Asserts.assertEquals(s,sou);

        //Threads.sleep(3000);

        buffer.closeAndDeleteFile();
	}
}

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
        System.out.println(FileBuffer.MAX_FILE_LENGTH % 12);
	}
}

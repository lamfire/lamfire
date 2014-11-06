package com.test.filequeue;

import com.lamfire.filequeue.FileList;
import com.lamfire.filequeue.FileListBuilder;
import com.lamfire.utils.Asserts;
import com.lamfire.utils.Bytes;


/**
 * Tests for QueueFile.
 * 
 * @author Bob Lee (bob@squareup.com)
 */
public class FileListTest {
	public static void main(String[] args) throws Exception {
        int buffSize = 8 * 1024 * 1024;

        FileListBuilder builder = new FileListBuilder();
        builder.dataDir("/data/FileQueue/").name("list2").indexBufferSize(buffSize).storeBufferSize(buffSize);
		FileList list = builder.build();
        list.clear();
        int count = 10000000;

        //add
        for(int i=0;i<count;i++){
            list.add(Bytes.toBytes(i));
            if(i % 100000 == 0){
                System.out.println("[add]"+i);
            }
        }

        System.out.println("[size]:"+list.size());

		byte[] bytes = list.get(0);
		Asserts.assertEquals(0, Bytes.toInt(bytes));

        //peek
        for(int i=0;i<count;i++){
            bytes = list.get(i);
            Asserts.assertEquals(i, Bytes.toInt(bytes));
            if(i % 100000 == 0){
                System.out.println("[peek]"+i);
            }
        }
		
		//size
        Asserts.assertEquals(count,list.size());


        System.out.println("[size]:"+list.size());
        list.delete();

	}
}

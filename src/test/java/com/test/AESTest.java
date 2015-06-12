package com.test;

import com.lamfire.code.AES;
import com.lamfire.utils.Asserts;

public class AESTest{


	public void testAES() {
		String data = "融资百亿四成投红罐 白云山增资不提绿盒";
		String key = "052E92D80523B70A";

        String enData = AES.encode(data,key);
        System.out.println("encode : "+enData);

        String deData = AES.decode(enData,key);
        System.out.println("decode : "+deData);

        Asserts.equalsAssert(data, deData);
	}


}

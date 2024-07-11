package com.lamfire.code;

import com.lamfire.utils.Bytes;

public class Base62 {
    public static String encode(long number) {  
        return Radixes.encode(number,62);  
    }  
  
    public static long decode(String base62) {  
        return Radixes.decode(base62,62);  
    }


    public static String toBase62String(byte[] bytes){
        StringBuilder buffer = new StringBuilder();
        for(int i=0;i<bytes.length;i+=8){
            byte[] b =  Bytes.subBytes(bytes,i,8);
            if(b.length < 8){
                byte[] n = new byte[8];
                Bytes.putBytes(n,8-b.length,b,0,b.length);
                b = n;
            }
            long val = Bytes.toLong(b);
            buffer.append(encode(val));
        }
        return buffer.toString();
    }
}  

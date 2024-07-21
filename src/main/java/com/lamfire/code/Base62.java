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
        return Radixes.toBaseXString(bytes,62);
    }

    public static byte[] decodeBase62String(String base62Str){
        return Radixes.decodeBaseXString(base62Str,62);
    }
}  

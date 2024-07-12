package com.lamfire.code;

import com.lamfire.utils.Bytes;

public class Radixes {
	static final String SS ="0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%*&=";
	static final int MAXRADIX = SS.length();

	public static String digest64(long value){
		return encode(value,64);
	}

	public static String digest62(long value){
		return encode(value,62);
	}

	public static String digest32(long value){
		return encode(value,32);
	}

	public static String digest16(long value){
		return encode(value,16);
	}
	
	public static String encode(long value,int radix){
		if(radix > MAXRADIX){
			throw new IllegalArgumentException("The parameter 'radix' cannot greater than [" + MAXRADIX +"]");
		}
		if(value ==0){
			return "0";
		}
		boolean negative = false; //如果为负数
		if(value < 0){
			value = Math.abs(value);
			negative = true;

		}
		StringBuilder buffer = new StringBuilder();
		while(value > 0){
			int v = (int)(value % radix);
			value = value / radix;
			buffer.append(SS.charAt(v));
		}
		if(negative){
			buffer.append('-');
		}
		return buffer.reverse().toString();
	}
	
	public static long decode(String text,int radix){
		if(text == null){
			return 0;
		}
		boolean negative = false; //如果为负数
		char i0 = text.charAt(0);
		if(i0 == '-'){
			negative = true;
			text = text.substring(1);
		}
		StringBuilder buffer = new StringBuilder(text);
		String source = buffer.reverse().toString();
		long result = 0,multiplier = 1;  
        for (int pos = 0; pos < source.length(); pos++) {
            result += ((SS.indexOf(source.substring(pos, pos+1))) * multiplier);
            multiplier *= radix;  
        }
		if(negative){
			return  -result;
		}
        return result;  
	}

	public static String toBaseXString(byte[] bytes,int radix){
		StringBuilder buffer = new StringBuilder();
		for(int i=0;i<bytes.length;i+=8){
			byte[] b =  Bytes.subBytes(bytes,i,8);
			if(b.length < 8){
				byte[] n = new byte[8];
				Bytes.putBytes(n,8-b.length,b,0,b.length);
				b = n;
			}
			long val = Bytes.toLong(b);
			buffer.append(encode(val,radix));
		}
		return buffer.toString();
	}
}

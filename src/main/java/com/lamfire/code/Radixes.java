package com.lamfire.code;

import com.lamfire.utils.Bytes;
import com.lamfire.utils.IOUtils;
import com.lamfire.utils.StringUtils;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class Radixes {
	static final String SS ="0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!#*$&%@=?|";

	static final int MAXRADIX = SS.length() - 1;

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
	
	public static String encode(final long sourVal,final int radix){
		if(radix > MAXRADIX){
			throw new IllegalArgumentException("The parameter 'radix' cannot greater than [" + MAXRADIX +"]");
		}
		if(radix < 10){
			throw new IllegalArgumentException("The parameter 'radix' cannot less than [10]");
		}
		if(sourVal ==0){
			return "0";
		}
		long value = sourVal;
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
	
	public static long decode(final String source,final int radix){
		if(StringUtils.isBlank(source)){
			return 0;
		}

		String text = source;
		boolean negative = false; //如果为负数
		char i0 = text.charAt(0);
		if(i0 == '-'){
			negative = true;
			text = text.substring(1);
		}
		StringBuilder buffer = new StringBuilder(text);
		String reverseText = buffer.reverse().toString();
		long result = 0,multiplier = 1;  
        for (int pos = 0; pos < reverseText.length(); pos++) {
            result += ((SS.indexOf(reverseText.substring(pos, pos+1))) * multiplier);
            multiplier *= radix;  
        }
		if(negative){
			return  -result;
		}
        return result;  
	}

	/**
	 * 获得分隔符
	 * 分隔符使用radix之后第一位字符作为分隔符
	 * @param radix
	 * @return
	 */
	static char getSeparator(int radix){
		if(radix > MAXRADIX){
			throw new IllegalArgumentException("The parameter 'radix' cannot greater than [" + MAXRADIX +"]");
		}
		return SS.charAt(radix);
	}
	public static String toBaseXString(final byte[] bytes,final int radix){
		StringBuilder buffer = new StringBuilder();
		char sepa = getSeparator(radix); //获得分隔符
		int subBytesLen=0;
		for(int i=0;i<bytes.length;i+=8){
			byte[] b =  Bytes.subBytes(bytes,i,8);
			subBytesLen = b.length;
			if(subBytesLen < 8){
				byte[] n = new byte[8];
				Bytes.putBytes(n,8-subBytesLen,b,0,b.length);
				b = n;
			}
			if(i >0)buffer.append(sepa);
			if(i + 8 >= bytes.length){
				buffer.append(subBytesLen);//为最后一个数据添加长度
			}
			long val = Bytes.toLong(b);
			String encode = encode(val,radix);
			buffer.append(encode);
		}
		return buffer.toString();
	}

	public static byte[] decodeBaseXString(final String encoded,final int radix){
		char sepa = getSeparator(radix); //获得分隔符
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] results;
		try {
			String[] frames = StringUtils.split(encoded, sepa);
			for (int i=0;i< frames.length;i++) {
				String frame = frames[i];
				int frameSize = 8;
				if( i == frames.length -1){
					frameSize = Integer.parseInt(StringUtils.substring(frame,0,1));
					frame = StringUtils.substring(frame,1);
				}
				long frameVal = decode(frame, radix);
				byte[] frameBytes = Bytes.toBytes(frameVal);
				if(frameSize < 8){
					frameBytes = Bytes.subBytes(frameBytes,frameBytes.length - frameSize,frameSize);
				}
				out.writeBytes(frameBytes);
			}
			results = out.toByteArray();
		}finally {
			IOUtils.closeQuietly(out);
		}
		return results;
	}
}

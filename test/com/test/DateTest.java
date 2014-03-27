package com.test;

import java.util.Date;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

import com.lamfire.utils.DateFormatUtils;
import com.lamfire.utils.DateUtils;

public class DateTest {

	public static void printTimeZone() {
		long millis = System.currentTimeMillis();
		String[] avaIds = TimeZone.getAvailableIDs();
		Set<String> ids = new TreeSet<String>();
		for(String id : avaIds){
			ids.add(id);
		}
		for(String id : ids){
			TimeZone timeZone = TimeZone.getTimeZone(id);
			System.out.println(id+ " : " + DateFormatUtils.format(millis, "yyyy-MM-dd HH:mm:dd",timeZone));
			System.out.println(String.format("%d-%d-%d", DateUtils.getYear(millis,timeZone), DateUtils.getMonth(millis,timeZone), DateUtils.getDay(millis,timeZone)));
			
		}
	}

	public static void main(String[] args) {
		printTimeZone();
		long millis = DateUtils.getZeroHourMillis(new Date());
		System.out.println(String.format("%d-%d-%d", DateUtils.getYear(millis), DateUtils.getMonth(millis), DateUtils.getDay(millis)));
		
		TimeZone timeZone = TimeZone.getTimeZone("Asia/Tokyo");
		System.out.println(String.format("%d-%d-%d", DateUtils.getYear(millis,timeZone), DateUtils.getMonth(millis,timeZone), DateUtils.getDay(millis,timeZone)));
		
	}
}

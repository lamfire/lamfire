package com.test;

import com.lamfire.utils.DateFormatUtils;
import com.lamfire.utils.DateUtils;

import java.util.*;

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

    static void print(Date date){
        System.out.println(DateFormatUtils.format(date,"yyyy-MM-dd HH:mm:ss"));
    }

	public static void main(String[] args) {
		Date date = new Date();
        print(date);

        DateUtils.setYear(date,1980);
        DateUtils.setMonth(date,9);
        DateUtils.setDay(date, 27);

        DateUtils.setHour(date, 14);
        DateUtils.setMinute(date,15);
        DateUtils.setSecond(date,16);


        print(date);

        DateUtils.setYearMonthDay(date,2012,9,1);
        DateUtils.setHourMinuteSecond(date,1,2,3);
        print(date);

        System.out.println(DateUtils.getHour(date.getTime()));
        System.out.println(DateUtils.getHour(date.getTime(),TimeZone.getTimeZone("Asia/Tokyo")));

        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        cal.set(Calendar.DAY_OF_MONTH,20);
        System.out.println(cal.getTime() + "  WEEK : " + cal.get(Calendar.DAY_OF_WEEK));

        System.out.println(DateUtils.isWorkdays(cal.getTimeInMillis()));
		
	}
}

package com.test;

import java.util.HashMap;
import java.util.Map;

import com.lamfire.utils.Maps;

public class MapsTest {

	public static void main(String[] args) {
		Map<String,Integer> map = new HashMap<String,Integer>();
		map.put("c", 23);
		map.put("d", 24);
		map.put("f", 22);
		map.put("e", 21);
		map.put("b", 22);
		
		
		for(Map.Entry<String, Integer> e : map.entrySet()){
			System.out.println(e.getKey() +" - " + e.getValue());
		}
		//System.out.println(JSON.toJSONString(map));
		
		System.out.println("==============================order by key");
		
		for(Map.Entry<String, Integer> e : Maps.getEntriesOrderByKey(map,true)){
			System.out.println(e.getKey() +" - " + e.getValue());
		}
		
		System.out.println("==============================order by value");
		
		for(Map.Entry<String, Integer> e : Maps.getEntriesOrderByValue(map,true)){
			System.out.println(e.getKey() +" - " + e.getValue());
		}
		//System.out.println(JSON.toJSONString(map));
	}
}

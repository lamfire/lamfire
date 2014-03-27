package com.test.json;

import com.lamfire.json.JSON;

public class JSONTest {

	public static void main(String[] args) {
		JSON js =  new JSON();
		js.put("c", 23);
		js.put("d", 24);
		js.put("f", 22);
		js.put("e", 21);
		js.put("b", 22);

		
		System.out.println(js.toJSONString());
	}
}

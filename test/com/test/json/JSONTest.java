package com.test.json;

import com.lamfire.json.JSON;

import java.util.Date;

public class JSONTest {

	public static void main(String[] args) {
		JSON js =  new JSON();
		js.put("c", 23);
		js.put("d", 24);
		js.put("f", 22);
		js.put("e", 21);
		js.put("b", 22);

        js.put("bytes","123456".getBytes());


        String json = js.toJSONString();
		System.out.println(json);

        js = JSON.fromJSONString(json);
        byte[] bytes = js.getByteArray("bytes");

        System.out.println(new String(bytes));



	}
}

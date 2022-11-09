package com.test.json;

import com.lamfire.json.JSON;
import com.lamfire.utils.Maps;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JSONTest {

	public static void main(String[] args) {
		Map<String,Object> map = Maps.newHashMap();
		Map<String,Object> map1 = Maps.newHashMap();
		map1.put("m","MMMM");

		map.put("1","1");
		map.put("m",map1);

		JSON js =  new JSON();
		js.put("c", 23);
		js.put("d", 24);
		js.put("f", 22);
		js.put("e", 21);
		js.put("b", 22);
		js.put("r", map);

        js.put("bytes","123456".getBytes());


        String json = js.toJSONString();
		System.out.println(json);

        js = JSON.fromJSONString(json);
        byte[] bytes = js.getByteArray("bytes");

        System.out.println(new String(bytes));



	}
}

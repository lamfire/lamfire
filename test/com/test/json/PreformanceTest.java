package com.test.json;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.lamfire.json.JSON;

public class PreformanceTest {

	public static Item getItem() {
		Item item = new Item();
		User user = new User();
		user.setAge(100);
		user.setName("lamfire");

		User user1 = new User();
		user1.setAge(101);
		user1.setName("hayash");

		List<User> users = new ArrayList<User>();
		users.add(user);
		users.add(user1);

		List<byte[]> values = new ArrayList<byte[]>();
		values.add("1".getBytes());
		values.add("2".getBytes());

		Args ags = new Args();
		ags.put("str", "value");
		ags.put("int", 12);
		ags.put("int2", 12);

		item.setKey("001");
		item.setUsers(users);
		item.setValues(values);
		item.setArgs(ags);
		item.setBytes("123456".getBytes());

		Set<String> keys = new HashSet<String>();
		keys.add("lamfire");
		keys.add("hayash");
		item.setKeys(keys);

		return item;
	}

	public static void serializeTest( ) {
		Item item = getItem();
		long startAt = System.currentTimeMillis();
		for(int i=0;i<10000000;i++){
			String js = JSON.toJSONString(item);
			if(i % 100000 == 0){
				long now = System.currentTimeMillis();
				System.out.println(i+ " items serialized,time :" + (now - startAt) +"ms => " + js);
				startAt = now;
			}
		}
	}
	
	public static void deserializeTest() {
		Item item = getItem();
		long startAt = System.currentTimeMillis();
		String js = JSON.toJSONString(item);
		for(int i=0;i<1000000000;i++){
			item = JSON.toJavaObject(js, Item.class); 
			if(i % 100000 == 0){
				long now = System.currentTimeMillis();
				System.out.println(i+ " items deserialized,time :" + (now - startAt) +"ms => " + item);
				startAt = now;
			}
		}
	}
	
	public static void main(String[] args) {
		deserializeTest();
	}
}

package com.test.json;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.lamfire.json.JSON;


public class SerializerTest  {
	private static final Item ITEM ;
	static{
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
		
		ITEM = new Item();
		ITEM.setKey("001");
		ITEM.setUsers(users);
		ITEM.setValues(values);
		ITEM.setArgs(ags);
		ITEM.setBytes("123456".getBytes());
		
		Set<String> keys =   new HashSet<String>();
		keys.add("lamfire");
		keys.add("hayash");
		ITEM.setKeys(keys);
	}
	
	public static String serializer(){
		JSON js = JSON.fromJavaObject(ITEM);
		return (js.toJSONString());
	}
	
	public static Item deserializer(){
		JSON js = JSON.fromJavaObject(ITEM);
		Item item = js.toJavaObject(Item.class);
		return item;		
	}
	
	public static void main(String[] args) {
		System.out.println(serializer());
		Item item = deserializer();
		System.out.println(JSON.toJSONString(item));
		
	}
}

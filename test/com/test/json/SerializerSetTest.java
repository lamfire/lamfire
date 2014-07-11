package com.test.json;

import com.lamfire.json.JSON;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class SerializerSetTest {
	static class SetItem {
        String key  ;
        Set<String> keys;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Set<String> getKeys() {
            return keys;
        }

        public void setKeys(Set<String> keys) {
            this.keys = keys;
        }
    }
	static SetItem getItem(){
        SetItem item = new SetItem();
        item.setKey("123");
		
		Set<String> keys =   new HashSet<String>();
		keys.add("lamfire");
		keys.add("hayash");
        item.setKeys(keys);
        return item;
	}
	
	public static String serializer(){
		JSON js = JSON.fromJavaObject(getItem());
		return (js.toJSONString());
	}
	
	public static Item deserializer(){
		JSON js = JSON.fromJavaObject(getItem());
		Item item = js.toJavaObject(Item.class);
		return item;		
	}
	
	public static void main(String[] args) {
		System.out.println(serializer());
		Item item = deserializer();
		System.out.println(JSON.toJSONString(item));
		
	}
}

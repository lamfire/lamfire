package com.test.json;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.lamfire.json.JSON;
import com.lamfire.utils.Asserts;
import com.lamfire.utils.Threads;

public class PreformanceTest {
    private static Item entity = newItem();
    private static String json = JSON.toJSONString(entity);

    public static Item getItem(){
        return entity;
    }

    public static Item newItem() {
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

    AtomicInteger counter = new AtomicInteger();

    Runnable statusThread = new Runnable() {
        int preOops = 0;
        @Override
        public void run() {
            int opsCount = counter.get();
            System.out.println("[count] = " +opsCount + " "+"[ops/s] = " + (opsCount - preOops));
            preOops = opsCount;
        }
    };

    public PreformanceTest(){
        Threads.scheduleWithFixedDelay(statusThread, 1, 1, TimeUnit.SECONDS);
    }

	public void serializeTest( ) {
		for(int i=0;i<10000000;i++){
			String js = JSON.toJSONString(entity);
            //System.out.println(js);
            counter.incrementAndGet();
			if(i  == 0){
				System.out.println(i+ "[serialized]:"  + js);
			}
		}
	}
	
	public void deserializeTest() {
        System.out.println("[deserialized]:" + json);
		for(int i=0;i<1000000000;i++){
			Item item = JSON.toJavaObject(json, Item.class);
            counter.incrementAndGet();
			if(i == 0){
                System.out.println(i+ "[deserialized]:"  + item);
			}
		}
	}
	
	public static void main(String[] args) {
        PreformanceTest test = new PreformanceTest();
        //test.serializeTest();
        test.deserializeTest();

        System.exit(0);
	}
}

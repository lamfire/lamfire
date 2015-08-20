package com.test.json;

import com.lamfire.json.JSON;
import com.lamfire.utils.Threads;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MutilThreadPreformanceTest {
    static String json ;

    static {
        Item item = getItem();
        json = JSON.toJSONString(item);
    }

    public static String getJSONString(){
        return json;
    }

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
        item.setUrl("https://itunes.apple.com/");

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

    public MutilThreadPreformanceTest(){
        Threads.scheduleWithFixedDelay(statusThread, 1, 1, TimeUnit.SECONDS);
    }

    ThreadPoolExecutor executor = Threads.newFixedThreadPool(18);

	public void serializeTest( ) {
	    for(int i=0;i<100;i++){
            executor.submit(serializer);
        }
	}

    private Runnable serializer = new Runnable() {
        @Override
        public void run() {
            Item item = getItem();
            String js = JSON.toJSONString(item);
            System.out.println(js);
            counter.incrementAndGet();
        }
    } ;

    private Runnable deserializer = new Runnable() {
        @Override
        public void run() {
            String js = getJSONString();
            Item item = JSON.toJavaObject(js,Item.class);
            counter.incrementAndGet();
        }
    } ;



	public static void main(String[] args) {
        MutilThreadPreformanceTest test = new MutilThreadPreformanceTest();
        test.serializeTest();
	}
}

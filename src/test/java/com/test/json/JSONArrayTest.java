package com.test.json;

import com.lamfire.json.JSON;
import com.lamfire.json.JSONArray;
import com.lamfire.utils.Lists;

import java.util.Date;
import java.util.List;

public class JSONArrayTest {
    public static void main(String[] args) {
        List<User> list = Lists.newArrayList();

        for(int i=0;i<3;i++){
            User u = new User();
            u.setAge(i);
            u.setName("u-" + i);
            u.setDate(new Date());
            list.add(u);
        }

        String json = JSON.toJSONString(list);
        System.out.println(json);

        JSONArray arr = JSON.toJSONArray(json);
        System.out.println(arr);


        Item item = new Item();
        item.setUsers(list);
        item.setKey("test");

        json = JSON.toJSONString(item);
        System.out.println(json);
        item = JSON.toJavaObject(json,Item.class);

        JSON js  = JSON.fromJSONString(json);

        JSONArray jarr = js.getJSONArray("users");
        System.out.println(jarr.size());


    }
}

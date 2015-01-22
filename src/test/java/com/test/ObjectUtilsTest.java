package com.test;

import com.lamfire.utils.ClassUtils;
import com.lamfire.utils.Lists;
import com.lamfire.utils.ObjectUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 15-1-15
 * Time: 上午9:50
 * To change this template use File | Settings | File Templates.
 */
public class ObjectUtilsTest {
    public static class Item implements Serializable {
        private String name;
        private int age;

        public Item(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    public static void timeTest() throws Exception {
        List<Object> newList = Lists.newArrayList();
        long newStartAt = System.currentTimeMillis();
        int count = 100000;
        for(int i=0;i<count;i++){
            newList.add(new Item(""+i));
        }
        System.out.println("new Object - " + (System.currentTimeMillis() - newStartAt));
        newList.clear();

        newStartAt = System.currentTimeMillis();
        for(int i=0;i<count;i++){
            newList.add(ClassUtils.newInstance(Item.class,""+i));
        }
        System.out.println("reflect Object - " + (System.currentTimeMillis() - newStartAt));
        newList.clear();

        Item item = new Item("name");
        item.setAge(232);
        newStartAt = System.currentTimeMillis();
        for(int i=0;i<count;i++){
            newList.add(ObjectUtils.clone(item));
        }
        System.out.println("clone Object - " + (System.currentTimeMillis() - newStartAt));
    }

    public static void test() throws Exception {
        Item item = new Item("name");
        item.setAge(232);
        ObjectUtils.setFieldValue(item,"name","lamfire test");
        System.out.println(ObjectUtils.getFieldValue(item,"name"));
        System.out.println("source : "+item);

        Item item2 = new Item("name1");
        ObjectUtils.copy(item, item2);
        System.out.println("copy : "+item2);

        ObjectUtils.empty(item);
        System.out.println("empty : "+(item));
    }

    public static void main(String[] args) throws Exception{
        timeTest();
    }
}

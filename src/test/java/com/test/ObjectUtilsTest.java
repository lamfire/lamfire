package com.test;

import com.lamfire.utils.Lists;
import com.lamfire.utils.ObjectUtils;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 15-1-15
 * Time: 上午9:50
 * To change this template use File | Settings | File Templates.
 */
public class ObjectUtilsTest {
    static class Item{
        private String name;
        private int age;

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

    public static void timeTest() throws IllegalAccessException, InstantiationException {
        List<Object> newList = Lists.newArrayList();
        long newStartAt = System.currentTimeMillis();
        int count = 1000000;
        for(int i=0;i<count;i++){
            newList.add(new Item());
        }
        System.out.println("new Object - " + (System.currentTimeMillis() - newStartAt));
        newList.clear();

        Class<?> c = Item.class;
        c.newInstance();
        newStartAt = System.currentTimeMillis();
        for(int i=0;i<count;i++){
            newList.add(c.newInstance());
        }
        System.out.println("reflect Object - " + (System.currentTimeMillis() - newStartAt));
    }

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        Item item = new Item();
        item.setAge(232);
        ObjectUtils.setFieldValue(item,"name","lamfire test");
        System.out.println(ObjectUtils.getFieldValue(item,"name"));
        System.out.println("source : "+item);

        Item item2 = new Item();
        ObjectUtils.copyJavaObject(item, item2);
        System.out.println("copy : "+item2);

        ObjectUtils.emptyJavaObject(item);
        System.out.println("empty : "+(item));
    }
}

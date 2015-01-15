package com.test;

import com.lamfire.utils.ObjectUtils;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static void main(String[] args) {
        Item item = new Item();
        ObjectUtils.setFieldValue(item,"name","lamfire test");
        System.out.println(ObjectUtils.getFieldValue(item,"name"));

        Item item2 = new Item();
        ObjectUtils.copyProperties(item, item2);
        System.out.println(ObjectUtils.getFieldValue(item2,"name"));
    }
}

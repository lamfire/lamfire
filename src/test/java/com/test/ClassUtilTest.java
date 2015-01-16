package com.test;

import com.lamfire.utils.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 15-1-16
 * Time: 上午11:00
 * To change this template use File | Settings | File Templates.
 */
public class ClassUtilTest {
    int a = 0;
    ArrayList<Integer> b = Lists.newArrayList();
    HashSet<Integer> c = Sets.newHashSet();
    HashMap<String,String> d = Maps.newHashMap();

    public static void main(String[] args) {
        Field aField = ClassUtils.getField(ClassUtilTest.class,"a");
        Asserts.assertEquals(ClassUtils.isGenericNumberType(aField.getType()),true);

        Field bField = ClassUtils.getField(ClassUtilTest.class,"b");
        Asserts.assertEquals(ClassUtils.isListType(bField.getType()),true);
        Asserts.assertEquals(ClassUtils.isCollectionType(bField.getType()),true);

        Field cField = ClassUtils.getField(ClassUtilTest.class,"c");
        Asserts.assertEquals(ClassUtils.isSetType(cField.getType()),true);
        Asserts.assertEquals(ClassUtils.isCollectionType(cField.getType()),true);

        Field dField = ClassUtils.getField(ClassUtilTest.class,"d");
        Asserts.assertEquals(ClassUtils.isMapType(dField.getType()),true);

        Asserts.assertEquals(ClassUtils.isAssignable(bField.getType(), Collection.class),true);
    }
}

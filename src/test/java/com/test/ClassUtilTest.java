package com.test;

import com.lamfire.utils.*;
import com.test.anno.TESTANNO;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 15-1-16
 * Time: 上午11:00
 * To change this template use File | Settings | File Templates.
 */
public class ClassUtilTest extends Client{
    int a = 0;
    ArrayList<Integer> b = Lists.newArrayList();
    HashSet<Integer> c = Sets.newHashSet();
    HashMap<String,String> d = Maps.newHashMap();

    List<?> list ;
    List oList;

    public static void a(String[] args) {
        Field aField = ClassUtils.getField(ClassUtilTest.class,"a");
        Asserts.equalsAssert(ClassUtils.isGenericNumberType(aField.getType()),true);

        Field bField = ClassUtils.getField(ClassUtilTest.class,"b");
        Asserts.equalsAssert(ClassUtils.isListType(bField.getType()),true);
        Asserts.equalsAssert(ClassUtils.isCollectionType(bField.getType()),true);

        Field cField = ClassUtils.getField(ClassUtilTest.class,"c");
        Asserts.equalsAssert(ClassUtils.isSetType(cField.getType()),true);
        Asserts.equalsAssert(ClassUtils.isCollectionType(cField.getType()),true);

        Field dField = ClassUtils.getField(ClassUtilTest.class,"d");
        Asserts.equalsAssert(ClassUtils.isMapType(dField.getType()),true);

        Asserts.equalsAssert(ClassUtils.isAssignable(bField.getType(), Collection.class),true);
    }

    @TESTANNO(path = "1")
    public static void a(int args) {

    }

    public static void main(String[] args) {
        Set<Method> methods = ClassUtils.getAllDeclaredMethodsByAnnotation(ClassUtilTest.class, TESTANNO.class);
        for(Method m : methods){
            StringBuffer buffer = new StringBuffer(m.getReturnType().getName()).append(" ").append(m.getName()).append("(");
            Class<?> [] types = m.getParameterTypes();
            int i=0;
            for(Class<?> t : types){
                if(i>0){
                    buffer.append(",") ;
                }
                buffer.append(t.getName());
                i++;
            }
            buffer.append(")");
            System.out.println(buffer.toString());
        }
    }
}

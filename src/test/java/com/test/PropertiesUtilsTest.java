package com.test;

import com.lamfire.json.JSON;
import com.lamfire.utils.PropertiesUtils;

public class PropertiesUtilsTest {
    public static void testLoadToStaticFields(String[] args) {
        PropertiesUtils.loadToStaticFields("conf.properties",Conf.class);
        System.out.println(Conf.enable);
        System.out.println(Conf.host);
        System.out.println(Conf.port);
        System.out.println(Conf.list);
        System.out.println(Conf.rate);

    }

    public static void main(String[] args) {
        Config c = new Config();
        PropertiesUtils.loadToObjectFields("conf.properties",c);
        System.out.println(JSON.toJSONString(c));
    }
}

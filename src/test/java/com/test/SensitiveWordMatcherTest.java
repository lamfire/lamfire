package com.test;

import com.lamfire.utils.KeywordMatcher;
import com.lamfire.utils.Lists;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 15-1-19
 * Time: 上午10:16
 * To change this template use File | Settings | File Templates.
 */
public class SensitiveWordMatcherTest {
    public static void main(String[] args) {
        List<String> list = Lists.newArrayList();
        list.add("中国");
        list.add("华人");
        list.add("坏蛋");
        list.add("变态");
        list.add("穷B");
        list.add("网民");
        list.add("愚蠢");

        KeywordMatcher matcher = new KeywordMatcher(list);

        String source = "中华人民共和国，没有坏蛋，也没有变态狂，只有一群可爱的网民．";

        System.out.println(matcher.match(source));
    }
}

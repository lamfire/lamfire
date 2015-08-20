package com.test;

import com.lamfire.utils.ClassLoaderUtils;
import com.lamfire.utils.FileUtils;
import com.lamfire.utils.KeywordMatcher;
import com.lamfire.utils.Lists;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 15-1-19
 * Time: 上午10:16
 * To change this template use File | Settings | File Templates.
 */
public class SensitiveWordMatcherTest {
    public static void test() {
        List<String> list = Lists.newArrayList();
        list.add("中国");
        list.add("华人");
        list.add("坏蛋");
        list.add("变态狂");
        list.add("穷B");
        list.add("网民");
        list.add("愚蠢");

        KeywordMatcher matcher = new KeywordMatcher(list);

        String source = "中华人民共和国，没有坏蛋，也没有变态狂，只有一群可爱的网民．";

        System.out.println(matcher.match(source));
        System.out.println(matcher.replace(source,'*'));
    }

    public static void main(String[] args) throws IOException {
        List<String> list = FileUtils.readLines(ClassLoaderUtils.getResourceAsFile("keyword",SensitiveWordMatcherTest.class));
        KeywordMatcher matcher = new KeywordMatcher(list);

        String source = "他 妈 的，中华人民共和国，没有坏蛋，也没有变态狂，只有一群可爱的网民．";

        long startAt = System.currentTimeMillis();
        matcher.match(source);
        System.out.println("times : " + (System.currentTimeMillis() - startAt) +"ms");

        String replace = matcher.replace(source,'*');
        System.out.println(replace);
    }
}

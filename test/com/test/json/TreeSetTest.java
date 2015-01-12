package com.test.json;

import com.lamfire.json.JSON;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 15-1-12
 * Time: 下午2:02
 * To change this template use File | Settings | File Templates.
 */
public class TreeSetTest {

    static class Entity{
        private HashSet<Integer> set;

        public Set<Integer> getSet() {
            return set;
        }

        public void setSet(HashSet<Integer> set) {
            this.set = set;
        }
    }

    public static void main(String[] args) {
        HashSet<Integer> set = new HashSet<Integer>();
        set.add(1);
        set.add(2);
        set.add(3);
        Entity e = new Entity();
        e.setSet(set);

        String js = JSON.toJSONString(e);
        System.out.println(js);

        Entity entity = JSON.toJavaObject(js,Entity.class);

        System.out.println(entity.getSet());
    }
}

package com.test.json;

import com.lamfire.json.JSON;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 15-1-12
 * Time: 下午2:02
 * To change this template use File | Settings | File Templates.
 */
public class CollectionTest {

    static class Entity{
        private Collection<Integer> collection;
        private List<Integer> list;
        private ArrayList<Integer> arrayList;
        private LinkedList<Integer> linkedList;

        private Set<Integer> set;
        private HashSet<Integer> hashSet;
        private TreeSet<Integer> treeSet;

        public HashSet<Integer> getHashSet() {
            return hashSet;
        }

        public void setHashSet(HashSet<Integer> hashSet) {
            this.hashSet = hashSet;
        }

        public LinkedList<Integer> getLinkedList() {
            return linkedList;
        }

        public void setLinkedList(LinkedList<Integer> linkedList) {
            this.linkedList = linkedList;
        }

        public Collection<Integer> getCollection() {
            return collection;
        }

        public void setCollection(Collection<Integer> collection) {
            this.collection = collection;
        }

        public List<Integer> getList() {
            return list;
        }

        public void setList(List<Integer> list) {
            this.list = list;
        }

        public ArrayList<Integer> getArrayList() {
            return arrayList;
        }

        public void setArrayList(ArrayList<Integer> arrayList) {
            this.arrayList = arrayList;
        }

        public Set<Integer> getSet() {
            return set;
        }

        public void setSet(Set<Integer> set) {
            this.set = set;
        }

        public TreeSet<Integer> getTreeSet() {
            return treeSet;
        }

        public void setTreeSet(TreeSet<Integer> treeSet) {
            this.treeSet = treeSet;
        }

        @Override
        public String toString() {
            return "Entity{" +
                    "collection=" + collection +
                    ", list=" + list +
                    ", arrayList=" + arrayList +
                    ", linkedList=" + linkedList +
                    ", set=" + set +
                    ", hashSet=" + hashSet +
                    ", treeSet=" + treeSet +
                    '}';
        }
    }

    static void fill(Collection<Integer> col){
        col.add(1);
        col.add(2);
        col.add(3);
        col.add(4);
        col.add(5);
    }

    public static void main(String[] args) {
        HashSet<Integer> hashSet = new HashSet<Integer>();
        fill(hashSet);

        TreeSet<Integer> treeSet = new TreeSet<Integer>();
        fill(treeSet);

        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        fill(arrayList);

        LinkedList<Integer> linkedList = new LinkedList<Integer>();
        fill(linkedList);

        Entity e = new Entity();
        e.setCollection(arrayList);
        e.setList(arrayList);
        e.setArrayList(arrayList);
        e.setLinkedList(linkedList);

        e.setSet(hashSet);
        e.setHashSet(hashSet);
        e.setTreeSet(treeSet);

        String js = JSON.toJSONString(e);
        System.out.println(js);

        Entity entity = JSON.toJavaObject(js,Entity.class);
        System.out.println(entity);
    }
}

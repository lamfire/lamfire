package com.test;

import com.lamfire.utils.CircularLinkedList;
import com.lamfire.utils.Threads;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * 双向循环链表
 * User: lamfire
 * Date: 14-10-27
 * Time: 上午11:40
 */
public class CircularLinkedListTest {
    static final CircularLinkedList<Integer> list = new CircularLinkedList<Integer>();

    public static void main(String[] args) {

        Threads.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                list.removeFirst();
            }
        },1,1, TimeUnit.SECONDS);

        for (int i = 0; i < 10; i++) {
            list.add(i);
        }

        System.out.println("[FILL]:" + list);

        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }

        for (int i = 0; i < 10; i++) {
            list.remove(i);
        }
        System.out.println("[REMOVED]:" + list);

        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        System.out.println("[FILL]:" + list);
        list.clear();
        System.out.println("[Clear]:" + list);

        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        System.out.println("[FILL]:" + list);


        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
            System.out.println("[REMOVE]:" + list);
        }

        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        System.out.println("[FILL]:" + list);


       it = list.descendingIterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
            System.out.println("[REMOVE]:" + list);
        }

        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        System.out.println("[FILL]:" + list);
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }

        CircularLinkedList<Integer> list2 = new CircularLinkedList<Integer>();
        for(int i=10;i<20;i++){
            list2.add(i);
        }
        list2.addAll(0, list);
        System.out.println("[FINISH]:" + list2);
        System.out.println("[FINISH]:" + list2.get(19));


        System.out.println("[FINISH]:" + list2.removeFirst());
        System.out.println("[FINISH]:" + list2.removeLast() );

        list2.addFirst(22);
        list2.addLast(20);
        System.out.println("[FINISH]:" + list2);

        System.out.println("[FINISH]:" + list2.contains(19));

        System.out.println("[FINISH]:" + list2.contains(18));


        while (it.hasNext()) {
            System.out.println("[LIST]:" + list + " - " + list.size());
            Threads.sleep(500);
        }

        System.out.println("[END]------------------------------------");
    }
}

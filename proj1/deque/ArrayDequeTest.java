package deque;

import org.junit.Test;

import java.util.TreeMap;

import static org.junit.Assert.assertEquals;

public class ArrayDequeTest {

    @Test
    public void addFirstTest0(){
        //when the ArrayDeque's cache is 0
       ArrayDeque<Integer> arr=new ArrayDeque<Integer>();
        arr.addFirst(3);
        Integer b = arr.get(0);
        Integer first = arr.get(0);
        assertEquals(b,first);

        //when the ArrayDeque's cache is not 0;
        ArrayDeque<Integer> arr1=new ArrayDeque<Integer>();
        arr1.addFirst(1);
        arr1.addFirst(3);
        Integer c = arr1.get(0);
        Integer first1 = 3;
        assertEquals(c,first1);
    }
    @Test
    public void addLastTest(){
        //when the ArrayDeque's cache is 0;
        ArrayDeque<Integer> arr=new ArrayDeque<Integer>();
        arr.addLast(1);
        int b=arr.get(0);
        int c=1;
        assertEquals(b,c);

        //when the ArrayDeque's cache is not 0;
        ArrayDeque<Integer> arr1=new ArrayDeque<Integer>();
        arr1.addLast(1);
        arr1.addLast(2);
        int b1=arr1.get(1);
        int c1=2;
        assertEquals(b1,c1);
    }
    @Test
    public void isEmptyTest(){
        //when the ArrayDeque's cache is 0;
        ArrayDeque<Integer> arr=new ArrayDeque<Integer>();
        assertEquals(true,arr.isEmpty());

        //when the ArrayDeque's cache is not 0;
        ArrayDeque<Integer> arr1=new ArrayDeque<Integer>();
        arr1.addFirst(1);
        assertEquals(false,arr1.isEmpty());
    }
    @Test
    public void sizeTest(){
        //when the ArrayDeque's cache is 0;
        ArrayDeque<Integer> arr=new ArrayDeque<Integer>();
        int a=0;
        assertEquals(a,arr.size());

        //when the ArrayDeque's cache is not 0;
        ArrayDeque<Integer> arr1=new ArrayDeque<Integer>();
        arr1.addFirst(1);
        arr1.addFirst(2);
        arr1.addFirst(3);
        int a1 = 3;
        assertEquals(a1,arr1.size());
    }

    @Test
    public void printDequeTest(){
        ArrayDeque<Integer> arr1=new ArrayDeque<Integer>();
        arr1.addFirst(1);
        arr1.addFirst(2);
        arr1.addFirst(3);
        arr1.printDeque();
    }

    @Test
    public void removeFirstTest(){
        ArrayDeque<Integer> arr=new ArrayDeque<Integer>();
        arr.addFirst(1);
        arr.addFirst(2);
        arr.removeFirst();
        int a = 1;
        int b = arr.get(0);
        assertEquals(a,b);
    }

    @Test
    public void removeLastTest(){
        ArrayDeque<Integer> arr=new ArrayDeque<Integer>();
        arr.addLast(1);
        arr.addLast(2);
        arr.addLast(3);
        arr.removeLast();
        int a = 2;
        int b = arr.get(1);
        assertEquals(a,b);
    }




}

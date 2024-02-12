package hashmap;

import org.junit.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.*;

public class test {
    @Test
    public void putTest(){
        MyHashMap<String, String> stringHashMap = new MyHashMap<>();
        assertEquals(0, stringHashMap.size());
        stringHashMap.put("abc","abcd");

        for (int i = 0; i < 455; i++) {
            stringHashMap.put("hi" + i, "a");
        }
        assertEquals(456, stringHashMap.size());
    }

    @Test
    public void getTest(){
        MyHashMap<String, String> stringHashMap = new MyHashMap<>();
        stringHashMap.put("abc","abcd");
        System.out.println(stringHashMap.get("abc"));
        System.out.println(stringHashMap.get("abcd"));
    }

    @Test
    public void putsTest()
    {

        MyHashMap<String, Integer> b = new MyHashMap<>();
        b.put("hi", 1);
       assertTrue(b.containsKey("hi") && b.get("hi") != null);

    }

    @Test
    public  void ContainsKeyTest() {
        MyHashMap<String, Integer> b = new MyHashMap<>();
        System.out.println("waterYouDoingHere".hashCode());
        System.out.println("waterYouDoingHere".hashCode()%16);
        assertFalse(b.containsKey("waterYouDoingHere"));
        b.put("waterYouDoingHere", 0);
        assertTrue(b.containsKey("waterYouDoingHere"));
    }

    @Test
    public void functionalityTest() {
        MyHashMap<String, Integer> studentIDs = new MyHashMap<>();
        MyHashMap<String, String> dictionary = new MyHashMap<>();
        assertEquals(0, dictionary.size());

        // can put objects in dictionary and get them
        dictionary.put("hello", "world");
        assertTrue(dictionary.containsKey("hello"));
        assertEquals("world", dictionary.get("hello"));
        assertEquals(1, dictionary.size());

        // putting with existing key updates the value
        dictionary.put("hello", "kevin");
        assertEquals(1, dictionary.size());
        assertEquals("kevin", dictionary.get("hello"));

        // putting key in multiple times does not affect behavior
        studentIDs.put("sarah", 12345);
        assertEquals(1, studentIDs.size());
        assertEquals(12345, studentIDs.get("sarah").intValue());
        studentIDs.put("alan", 345);
        assertEquals(2, studentIDs.size());
        assertEquals(12345, studentIDs.get("sarah").intValue());
        assertEquals(345, studentIDs.get("alan").intValue());
        studentIDs.put("alan", 345);
        assertEquals(2, studentIDs.size());
        assertEquals(12345, studentIDs.get("sarah").intValue());
        assertEquals(345, studentIDs.get("alan").intValue());
        studentIDs.put("alan", 345);
        assertEquals(2, studentIDs.size());
        assertEquals(12345, studentIDs.get("sarah").intValue());
        assertEquals(345, studentIDs.get("alan").intValue());
        assertTrue(studentIDs.containsKey("sarah"));
        assertTrue(studentIDs.containsKey("alan"));

        // handle values being the same
        assertEquals(345, studentIDs.get("alan").intValue());
        studentIDs.put("evil alan", 345);
        assertEquals(345, studentIDs.get("evil alan").intValue());
        assertEquals(studentIDs.get("evil alan"), studentIDs.get("alan"));
    }
    @Test
    public void keySetTest(){
        MyHashMap<String, Integer> b = new MyHashMap<>();
        HashSet<String> values = new HashSet<String>();
        b.put("a",1);
        b.put("b",2);
        b.put("c",3);
        values.add("a");
        values.add("b");
        values.add("c");
        Set<String > c = b.keySet();
        Iterator itor = c.iterator();
        while(itor.hasNext()) System.out.println((String)itor.next());;
        // System.out.println(values.containsAll(c));

    }

}



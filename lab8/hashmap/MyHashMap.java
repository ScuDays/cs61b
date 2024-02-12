package hashmap;

import java.security.Key;
import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amor tized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    //清空所有桶
    @Override
    public void clear() {
        for (int i = 0; i < CollectionSize; i++)buckets[i] = createBucket();
        this.cache = 0;
    }

    @Override
    public boolean containsKey(K key) {
        int place = key.hashCode();
        if(place < 0)place = place * -1;
        place = place % CollectionSize;
        if (buckets[place] == null)return false;
        Iterator itor = buckets[place].iterator();
        while(itor.hasNext()){
            Node a = (Node)itor.next();
            if (a.key.equals(key))return true;
        }
        //不存在该键值
        return false;
    }

    @Override
    public V get(K key) {
        int place = key.hashCode();
        if(place < 0)place = place * -1;
        place = place % CollectionSize;
        Iterator itor = buckets[place].iterator();
        while(itor.hasNext()){
            Node a = (Node)itor.next();
            if (a.key.equals(key))return a.value;
        }
        //不存在该键值
        return null;
    }

    @Override
    public int size() {
        return cache;
    }

    @Override
    public void put(K key, V value) {
        if(this.containsKey(key)){
            int place = key.hashCode();
            if(place < 0)place = place * -1;
            place = place % CollectionSize;
            Iterator itor = buckets[place].iterator();
            while(itor.hasNext()){
                Node a = (Node)itor.next();
                if (a.key.equals(key)) {
                    a.value = value;
                    break;
                }
            }

        }
        else {
            cache++;
            if (cache / CollectionSize >= maxLoad) resize();
            Node AddNode = createNode(key, value);
            int place = AddNode.key.hashCode();
            if (place < 0) place = place * -1;
            place = place % CollectionSize;
            buckets[place].add(AddNode);
        }
    }

    @Override
    public Set<K> keySet() {
        HashSet<K> aHashSet = new HashSet<>();
        for(int i = 0; i < CollectionSize; i++){
            Iterator itor = buckets[i].iterator();
            while(itor.hasNext())aHashSet.add(((Node)itor.next()).key);
        }
        return (Set<K>) aHashSet;
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        Set Aset = keySet();
        return Aset.iterator();
    }


    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    //无需修改，简单的键类
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!

    //最大负载函数
    private double maxLoad = 0.75;
    //当前哈希数组大小
    private int CollectionSize = 16;
    private int cache = 0;

    /** Constructors */
    public MyHashMap() {
        buckets = createTable(CollectionSize);
        for(int i = 0; i < CollectionSize; i++) buckets[i] = createBucket();
    }

    public MyHashMap(int initialSize) {
        this.CollectionSize  = initialSize;
        this.maxLoad = 0.75;
        buckets = createTable(CollectionSize);
        for(int i = 0; i < CollectionSize; i++) buckets[i] = createBucket();
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     *      * The load factor (# items / # buckets) should always be <= loadFactor
     *      *
     *      * @param initialSize initial size of backing array
     *      * @param maxLoad maximum load factor
     *      */


    public MyHashMap(int initialSize, double maxLoad) {
        this.CollectionSize = initialSize;
        this.maxLoad = maxLoad;
        buckets = createTable(CollectionSize);
        for(int i = 0; i < CollectionSize; i++) buckets[i] = createBucket();
    }



    /**
     *      * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }




    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new ArrayList<Node>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return new Collection[tableSize];
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

    //重新调整数组大小
    private void resize(){
        int newCollectionSzie  = this.CollectionSize * 2;
        Collection<Node>[] newTable = this.createTable(newCollectionSzie);
        for(int i = 0; i < newCollectionSzie; i++) newTable[i] = createBucket();
        for(int i = 0; i < CollectionSize; i++) {
            Iterator itor = buckets[i].iterator();
            while (itor.hasNext()) {
                Node AddNode = (Node) itor.next();
                int place = AddNode.hashCode() % CollectionSize;
                newTable[place].add(AddNode);
            }
        }
        buckets = newTable;
        this.CollectionSize = newCollectionSzie;
    }

}

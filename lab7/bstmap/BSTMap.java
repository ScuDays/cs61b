package bstmap;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;


public class BSTMap<K extends Comparable<K>,V> implements Map61B<K,V>  {
    private K key;
    private V value;
    private BSTMap<K,V> left;
    private BSTMap<K,V> right;
    private int cache;

    public BSTMap(){
        this.key = null;
        this.right = null;
        this.left = null;
        this.cache = 0;
        this.value = null;
    }
    public BSTMap(K key,V value){
        this.key = key;
        this.value = value;
        this.left = null;
        this.right = null;
        this.cache = 0;
    }
    @Override
    public void clear() {
        this.left = null;
        this.right = null;
        this.key = null;
        this.cache = 0;


    }

    @Override
    public boolean containsKey(K key) {
        if (this.key == null)return false;
        if(this.key.equals(key))return true;
        if(key.compareTo(this.key) < 0){
            if(this.left == null)return false;
            return this.left.containsKey(key);
        }

        else if(key.compareTo(this.key) > 0){
            if (this.right == null) return false;
            return this.right.containsKey(key);
        }
        return false;
    }

    @Override
    public V get(K key) {
        if(this.key == null)return null;
        if(this.key.equals(key))return this.value;
        if(key.compareTo(this.key) < 0){
            if(this.left == null)return null;
            return this.left.get(key);
        }

        else if(key.compareTo(this.key) > 0){
            if (this.right == null) return null;
            return this.right.get(key);
        }
        return null;
    }

    @Override
    public int size() {
        return cache;
    }

    @Override
    public void put(K key, V value) {
     insert(this ,key ,value);
     this.cache ++ ;

    }

   private BSTMap insert(BSTMap bst, K key, V value) {

       if (bst == null) return new BSTMap(key, value);
       if (bst.key == null) {
           bst.key = key;
           bst.value = value;
       } else if (bst.key.compareTo(key) > 0)
           bst.left = insert(bst.left, key, value);
       else if (bst.key.compareTo(key) < 0)
           bst.right = insert(bst.right, key, value);
       return bst;
   }
       public void printInOrder(){
           // if(this.left == null && this.right == null)  return;;
            if(this.left != null) this.left.printInOrder();
            else if(this.right != null)this.right.printInOrder();
            System.out.println(this.key);
            return;
       }
    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }
}

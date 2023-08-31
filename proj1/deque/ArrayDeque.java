package deque;

import java.util.Arrays;
import java.util.Iterator;

public class ArrayDeque<T> implements Iterable{


    private int size;
    private int cache;
    private Object[] arr;
    public ArrayDeque(){
        //不能直接实例化T类型数组，所以创建一个object数组，后面在获取元素的时候用强制类型转换来进行维护。
      arr=new Object[8];
      size=8;
      cache=0;
    }

    public void addFirst(T item){
        cache++;
        if(size==0){
            arr[0]=item;
        }
        else{
            if(cache>=size){
                size=size*2;
                Object[] arr2=new Object[size];
                arr2[0]=item;
                System.arraycopy(arr, 0, arr2, 1, cache - 1);
                arr=arr2;
            }
            else {
                Object[] arr2 = new Object[size];
                arr2[0] = item;
                System.arraycopy(arr, 0, arr2, 1, cache - 1);
                arr = arr2;
            }
        }
    }

    public void addLast(T item){
        cache++;
        if(cache>=size){
            size*=2;
            Object[] arr2=new Object[size];
            arr2[cache-1]=item;
            System.arraycopy(arr, 0, arr2, 0, cache - 1);
            arr=arr2;
        }
        else{
            arr[cache-1]=item;
        }
    }

    public boolean isEmpty(){
        return cache==0;
    }

    public int size(){
        return this.cache;
    }

    public void printDeque(){
        for(int i=0;i<cache;i++){
            System.out.print((T)arr[i]+" ");
        }
    }

    public T removeFirst(){

        if(cache==0) return null;
        else{
            T returnElement = (T)arr[0];
            Object[] arr2=new Object[size];
            System.arraycopy(arr, 1, arr2, 0, cache-1);
            arr=arr2;
            cache--;
            return  returnElement;
        }
    }

    public T removeLast(){
        if(cache==0) return null;
        else{
            T returnElement = (T)arr[cache-1];
            arr[cache-1]=null;
            cache--;
            return returnElement;
        }
    }

    public T get(int index){
        if(index <= 0) return null;
        return (T)arr[index-1];
    }

    @Override
    public Iterator iterator() {

        return new ArrayDequeIterator();
    }
    public class ArrayDequeIterator implements Iterator<T>{
        int wiz=0;

        @Override
        public boolean hasNext() {
            if(wiz<cache) {
                return true;
            }
            return false;
        }

        @Override
        public T next() {
            wiz++;

            return (T)arr[wiz-1];
        }
    }
}

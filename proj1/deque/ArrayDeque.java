
/** the first version which is not a circular array. */
/*
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
            if(size >= 16 && ( (double) size /cache > 0.25)){
                T returnElement = (T)arr[0];
                Object[] arr2=new Object[cache];
                System.arraycopy(arr, 1, arr2, 0, cache-1);
                arr=arr2;
                cache--;
                return  returnElement;
            }
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
            if(size >= 16 && ( (double) size /cache > 0.25)){
            T returnElement = (T)arr[cache-1];
            Object[] arr2=new Object[cache];
            System.arraycopy(arr, 0, arr2, 0, cache-1);
            arr=arr2;
            cache--;
            return returnElement;
        }
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
    private class ArrayDequeIterator implements Iterator<T>{
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
*/


/** the second version which is circular array
 */


package  deque;

import java.lang.reflect.Array;
import java.util.Iterator;

public class ArrayDeque<T> implements Iterable
{
     int first;
     int last;
     Object[] arr;
    int cache;
    int size;
    int wiz;


    public ArrayDeque()
    {

        cache = 0;
        first = 4;
        last = 4;
        arr = new Object[8];
        size = 8;
    }

    public void reSize(){
        if(first == 0){
            int sizeturn = size * 2;
            Object[] arrCopy = new Object[sizeturn];
            System.arraycopy(arr,0,arrCopy,0,cache);
            arr = arrCopy;
            size *= 2;
        }
        else {
            int sizeturn = size * 2;
            Object[] arrCopy = new Object[sizeturn];
            System.arraycopy(arr,0,arrCopy,0,last + 1);
            System.arraycopy(arr,first,arrCopy,last + size + 1 ,cache - first);
            first = first + size;
            size *= 2;
        }



    }
    public void addFirst(T item)
    {
       if(cache == size){
          this.reSize();
       }
       if(arr [first] == null){
           arr [first] = item;
       }
       else if(first == 0){
           first = size -1;
           arr[first] = item;
       }
       else{
           first = first - 1;
           arr[first] = item;
       }
       cache ++;

    }
    public void addLast(T item)
    {
        if(cache == size) {
            this.reSize();
        }
        if(arr[last] == null){
            arr[last] = item;
        }
         else if(last == size - 1){
             last = 0;
             arr[last] = item;
         }
         else{
             last = last + 1;
             arr[last] = item;
         }
         cache ++;
    }
    public int size(){
        return cache;
    }
    public void printDeque(){
        if(first < size){
            for(int i = first ; i <= last ; i ++){
                System.out.println(arr[i]);
            }
        }
        else{
            for(int i = first ; i<= size-1 ; i++ ) System.out.println(arr[i]);
            for(int i = 0; i <= last ; i++ ) System.out.println(arr[i]);
        }
    }
    public T removeFirst(){

        if(cache == 0){
            return null;
        }
        T a = (T) arr[first];
        if(first == size - 1){
            arr[first] = null;
            first =0;
        }
        else{
            arr[first] =null;
            first ++;
        }
        cache --;
        return a;
    }

    public T removeLast(){

        if(cache == 0){
            return null;
        }
        T a = (T) arr[last];
        if(last == 0){
            arr[last] = null;
            last = size - 1;
        }
        else{
            arr[last] =null;
            last --;
        }
        cache --;
        return a;
    }
    public T get(int index){
        index ++;
        if(index > cache)return null;
        if(first < last)return (T) arr[first + index - 1];
        else if(size - first >= index)
        {
            return (T) arr[first + index - 1];
        }
        else return (T) arr[index - (size - first) - 1];
    }


    @Override
    public Iterator iterator() {
        return new ArrayDequeIterator();
    }
    public class ArrayDequeIterator implements Iterator{
        public ArrayDequeIterator(){
            wiz = first;
        }

        @Override
        public boolean hasNext() {
                if(wiz == last+1 && wiz +1 != first)return false;
                else return true;
        }

        @Override
        public T next() {
            if(wiz == size-1){
                wiz = 0 ;
                T a = (T) arr[size - 1];
                return a;
            }
            else {
                wiz ++;
                T a =(T) arr[wiz - 1];
                return a;
            }
        }
    }

}
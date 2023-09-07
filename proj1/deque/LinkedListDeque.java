/*
package deque;

public class LinkedListDeque<T> {

    private IntNode sentinel;
    private int cache;
    public  LinkedListDeque(){
        sentinel =new IntNode();
        sentinel.next=null;
        sentinel.first=null;
        this.cache=0;


    }

    public void addFirst(T i) {
        IntNode a=new IntNode();
        a.first=i;
        sentinel.next=a;
        cache++;

    }

    public T removeFirst() {
        IntNode a;
        if (sentinel.next!=null) {
            a = sentinel.next.next;
            T Return =sentinel.next.first;
            sentinel.next=a;
            cache--;
            return  Return;
        }
        else{
            sentinel.next=null;
            cache=0;
            return null;
        }

    }

    public boolean isEmpty() {
        if(cache==0)return true;
        else return false;

    }

    public void addLast(T i) {
        IntNode a=new IntNode();
        a.first=i;
        IntNode finalNode =sentinel;
        while(finalNode.next!=null)finalNode=finalNode.next;
        finalNode.next=a;
        cache++;

    }

    public int size() {
        return cache;
    }

    public void printDeque() {

        if(sentinel.next==null){
            return;
        }
        IntNode finalNode=sentinel;
        while(finalNode.next!=null){
            finalNode=finalNode.next;
            System.out.println(finalNode.first);
        }
        System.out.println(finalNode.first);
    }

    public T removeLast() {
        if(cache==0)return null;

        IntNode finalNode =sentinel.next;
        while(finalNode.next!=null){
            finalNode=finalNode.next;

        }
        T Return =finalNode.first;
        finalNode=null;
        cache--;
        return Return;

    }

    public class IntNode{
       T first;
       IntNode next;

       public IntNode(){
           this.first=null;
           this.next=null;
       }
       public IntNode(T first){
           this.first=first;

       }
   }

}
*/



package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Iterable<T>{

    private IntNode sentinel;
    private IntNode sentinelLast;
    private IntNode getHelp;
    private IntNode IteratorHelp;
    private int cache;
    private int wiz;

    private class IntNode{
        IntNode last;
        T own;
        IntNode next;

        public IntNode(){
            this.last=null;
            this.own=null;
            this.next=null;
        }
        public IntNode(T own){
            this.last=null;
            this.next=null;
            this.own=own;
        }
    }

    //实现Iterable接口的方法
    @Override
    public Iterator<T> iterator() {

        return new LinkedListDequeIterator();
    }
    private class LinkedListDequeIterator implements Iterator<T>{

        public LinkedListDequeIterator(){
            IteratorHelp= new IntNode();
            IteratorHelp.next=sentinel.next;
        }
        @Override
        public boolean hasNext() {
            return wiz<cache;
        }

        @Override
        public T next() {
            if(hasNext() == false){
                throw new IllegalArgumentException("there is not an element");
            }
            else{
                wiz++;
                T a=IteratorHelp.next.own;
                IteratorHelp.next=IteratorHelp.next.next;
                return a;
            }
        }
    }



    public  LinkedListDeque(){
        sentinel = new IntNode();
        sentinelLast = new IntNode();
        getHelp = new IntNode();
        sentinel.next=sentinelLast;
        sentinelLast.last=sentinel;

        this.cache=0;

    }

    public void addFirst(T i) {
        IntNode a=new IntNode();
        a.own=i;
        if(cache == 0){
            sentinel.next = a;
            a.last = sentinel;
            a.next = sentinel;
            sentinelLast.last = a;
        }
        else{
            IntNode b = sentinel.next;
            sentinel.next = a;
            a.last = sentinel;
            a.next = b;
            b.last = a;
        }

        cache++;

    }

    public T removeFirst() {
        IntNode a;
        if (cache!=0) {
            a = sentinel.next.next;
            T Return = sentinel.next.own;
            sentinel.next = a;
            a.last = sentinel;
            cache--;
            return  Return;
        }
        return null;


    }

  /*  public boolean isEmpty() {
        if(cache==0)return true;
        else return false;
    }*/

    public void addLast(T i) {
        IntNode add=new IntNode();
        add.own=i;
        sentinelLast.last.next=add;
        add.last=sentinelLast.last;
        add.next=sentinelLast;
        sentinelLast.last=add;

        cache++;

    }

    public T removeLast() {
        if(cache==0)return null;

        IntNode finalNode =sentinelLast.last;
        T Return =finalNode.own;
        sentinelLast.last=sentinelLast.last.last;

        sentinelLast.last.next=sentinelLast;
        cache--;
        return Return;

    }

    public int size() {
        return cache;
    }

    public void printDeque() {

        if(cache==0){
            return;
        }
        IntNode finalNode=sentinel;
        int cache1=cache;
        while(cache1!=0){
            finalNode=finalNode.next;
            System.out.println(finalNode.own);
            cache1--;
        }
    }

    public T get(int number){

        if(number > this.cache)throw new IllegalArgumentException("the number didn't exist");
        IntNode ReturnElement = sentinel;
        for(int i = 0;i < number;i++) {
            ReturnElement=ReturnElement.next;
        }
        return  ReturnElement.own;
    }
    public T getRecursive(int number){
        if(getHelp.next == null){
           getHelp = new IntNode();
           getHelp.next = sentinel.next;
        }

        if(number==1){
            T a = getHelp.next.own;
            getHelp.next = null;
            return a;
        }
        else {
           this.getHelp.next = this.getHelp.next.next;
           number--;
           return this.getRecursive(number);
        }

    }

    public boolean equals(Object a){
        if(a.getClass()!=this.getClass())return false;
        LinkedListDeque<T> b = (LinkedListDeque <T>)a;
        if(b.size() != this.cache)return false;
        for(int i = 0;i < this.size() ; i++){
            if(b.get(i) != this.get(i)) return false;
        }
        return true;

    }




}


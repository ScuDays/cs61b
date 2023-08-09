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

public class LinkedListDeque<T> {

    private IntNode sentinel;
    private IntNode sentinelLast;
    private int cache;

    public class IntNode{
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



    public  LinkedListDeque(){
        sentinel =new IntNode();
        sentinelLast =new IntNode();

        sentinel.next=sentinelLast;
        sentinelLast.last=sentinel;

        this.cache=0;

    }

    public void addFirst(T i) {
        IntNode a=new IntNode();
        a.own=i;
        if(cache==0){
            sentinel.next=a;
            a.last=sentinel;
            a.next=sentinel;
            sentinelLast.last=a;
        }
        else{
            IntNode b=sentinel.next;
            sentinel.next=a;
            a.last=sentinel;
            a.next=b;
            b.last=a;
        }

        cache++;

    }

    public T removeFirst() {
        IntNode a;
        if (cache!=0) {
            a = sentinel.next.next;
            T Return =sentinel.next.own;
            sentinel.next=a;
            a.last=sentinel;
            cache--;
            return  Return;
        }
        return null;


    }

    public boolean isEmpty() {
        if(cache==0)return true;
        else return false;
    }

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




}


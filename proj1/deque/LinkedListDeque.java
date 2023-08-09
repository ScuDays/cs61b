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

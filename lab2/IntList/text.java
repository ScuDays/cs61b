package IntList;

import static IntList.IntListExercises.addConstant;

public class text {


    public static void main(String[] args) {
        IntList list1=new IntList(1,null);
        IntList list2=new IntList(2,list1);
        IntList list3=new IntList(3,list2);
        IntList list4=new IntList(4,list3);
        System.out.println(list4.get(0));
        System.out.println(list4.get(1));
        AddConstantTest a=new AddConstantTest();
        //a.testAddConstantOne();
        a.testAddConstantTwo();
        a.testAddToLargeList();

    }
}

package UnionFind;

import org.junit.Test;

public class UnionFindTest {
    @Test
    public void unionTest(){
        UnionFind a = new UnionFind(10);
        a.union(1,3);
       // a.union(2,4);
        //a.union(2,1);
        a.union(1,3);
        System.out.println(a.parent(2));
    }
}

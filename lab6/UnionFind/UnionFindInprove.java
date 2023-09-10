package UnionFind;

public class UnionFindInprove {

    // TODO - Add instance variables?

    private int[] arr;
    private int cache;

    /* Creates a UnionFind data structure holding n vertices. Initially, all
       vertices are in disjoint sets. */
    public UnionFindInprove(int n) {
        // TODO
        arr = new int[n];
        cache = n;
        for (int i = 0; i < n; i++) arr[i] = -1;
    }

    /* throws an exception if v1 is not a valid index. */
    private void validate(int vertex) {
        // TODO
        if (vertex > this.cache) throw new IllegalArgumentException("don't exeit");
    }

    /* Returns the size of the set v1 belongs to. */
    public int sizeOf(int v1) {
        // TODO
        int a = find(v1);
        return arr[a];
    }

    /* Returns the parent of v1. If v1 is the root of a tree, returns the
       negative size of the tree for which v1 is the root. */
    public int parent(int v1) {
        // TODO
        int a = find(v1);
        if (a == v1) return arr[v1];
        else return a;
    }

    /* Returns true if nodes v1 and v2 are connected. */
    public boolean connected(int v1, int v2) {
        // TODO
        int parent1 = find(v1);
        int parent2 = find(v2);
        if (parent1 == parent2) return true;
        return false;
    }

    /* Connects two elements v1 and v2 together. v1 and v2 can be any valid 
       elements, and a union-by-size heuristic is used. If the sizes of the sets
       are equal, tie break by connecting v1's root to v2's root. Unioning a 
       vertex with itself or vertices that are already connected should not 
       change the sets but may alter the internal structure of the data. */
    public void union(int v1, int v2) {
        // TODO
        int parent1 = find(v1);
        int parent2 = find(v2);
        if (arr[parent1] < arr[parent2]) {
            arr[parent1] = arr[parent1] + arr[parent2];
            arr[parent2] = parent1;
        }
        arr[parent2] = arr[parent1] + arr[parent2];
        arr[parent1] = parent2;
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. */
    public int find(int vertex) {
        // TODO
        int a = vertex;
        while (arr[a] > 0) a = arr[a];
        return a;
    }


    public static void main(String[] args) {
        UnionFindInprove a = new UnionFindInprove(10);
        a.union(1, 3);
        System.out.println(a.connected(1, 3));
    }
}
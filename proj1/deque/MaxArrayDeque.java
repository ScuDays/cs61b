package deque;

import org.junit.Test;

import java.util.Comparator;

import static edu.princeton.cs.algs4.StdIn.isEmpty;

public class MaxArrayDeque<T> extends ArrayDeque<T>{

    private final Comparator<T> comparator;
    public MaxArrayDeque(Comparator<T> c){
        comparator = c;
    }
    public T max(Comparator<T> c) {
        if (isEmpty()) {
            return null;
        }
        int maxIndex = 0;
        for (int i = 1; i < size(); i++) {
            if (c.compare(get(i), get(maxIndex)) > 0) {
                maxIndex = i;
            }
        }
        return get(maxIndex);
    }

    public T max() {
        return max(comparator);
    }




}

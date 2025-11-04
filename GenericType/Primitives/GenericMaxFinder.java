import java.util.*;

public class GenericMaxFinder {
    public static <T extends Comparable<T>> T findMax(List<T> list) {
        if (list.isEmpty()) {
            return null;
        }
        T max = list.get(0);
        for (T item : list) {
            if (item.compareTo(max) > 0) {
                max = item;
            }
        }
        return max;

    }

    public static void main(String[] args) {
       List<Integer> numbers = Arrays.asList(4, 15, 8, 23, 16);
       Integer maxNumber=findMax(numbers);
         System.out.println("Maximum Integer: " + maxNumber);
    }
}

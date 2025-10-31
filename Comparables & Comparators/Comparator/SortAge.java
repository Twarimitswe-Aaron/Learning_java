package Comparator;
import java.util.*;;

public class SortAge implements Comparator<Employee> {
    @Override
    public int compare(Employee e1, Employee e2) {
        return Integer.compare(e1.getAge(), e2.getAge());
    }

}

package Comparable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EmployeeMain {
    public static void main(String[] args) {
        List<Employee> list = new ArrayList<>();
        list.add(new Employee("Twarimitswe", "Aaron", 18, 30000000));
        list.add(new Employee("Amani", "Sam", 12, 10040000));
        list.add(new Employee("Berald", "Aaron", 28, 20000000));

        // Sort using Comparable (by age)
        Collections.sort(list, new SortBySalary());


        System.out.println("Employees list sorted by age:");
        for (Employee e : list) {
            System.out.println(e);
        }
    }
}

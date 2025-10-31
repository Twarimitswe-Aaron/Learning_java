package Comparator;

import java.util.*;

public class EmployeeMain {
    public static void main(String[] args) {
        List<Employee> list = new ArrayList<>();
        list.add(new Employee("Twarimitswe", "Aaron", 18, 30000000));
        list.add(new Employee("Amani", "Sam", 12, 10040000));
        list.add(new Employee("Berald", "Aaron", 28, 20000000));

        Collections.sort(list, new SortAge());
        System.out.println("Employees list sorted by age:");
        System.out.println(list);

        Comparator<Employee> com = new Comparator<Employee>() {
            @Override
            public int compare(Employee e1, Employee e2) {
                if (e1.getSalary() >= e2.getSalary()) {
                    return 1;

                } else {
                    return -1;
                }
            }
        };

        Collections.sort(list, com);
        System.out.println("Employees list sorted by salary:");
        System.out.println(list);

        Employee em1=new Employee("Twarimitswe", "Aaron", 18, 30000000);
        Employee em2=new Employee("Amani", "Sam", 12, 10040000);


        Set<Employee> empSet = new HashSet<>();
        empSet.add(em1);
        empSet.add(em2);
        boolean var=em1.equals(em2);
        System.out.println("Are em1 and em2 equal? "+var);
        System.out.println(empSet.size());

        Map<Employee,String> empMap=new HashMap<>();
        empMap.put(em1,"Developer");
        empMap.put(em2,"Manager");

    }
}

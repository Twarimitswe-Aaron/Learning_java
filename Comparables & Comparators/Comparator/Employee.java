package Comparator;
public class Employee {
    private String firstName;
    private String lastName;
    private int age;
    private int salary;

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public Employee(String firstName, String lastName, int age, int salary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", salary=" + salary +
                '}';
    }

    //getters

    public int getAge() {
        return this.age;

    }

    public int getSalary() {
        return this.salary;
    }

}
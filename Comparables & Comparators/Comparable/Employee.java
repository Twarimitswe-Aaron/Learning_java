package Comparable;

public class Employee implements Comparable<Employee> {
    private String firstName;
    private String lastName;
    private int age;
    private int salary;

    // Constructor
    public Employee(String firstName, String lastName, int age, int salary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.salary = salary;
    }

    // Getters
    public int getAge() {
        return age;
    }

    public int getSalary() {
        return salary;
    }

    // Setter
    public void setSalary(int salary) {
        this.salary = salary;
    }

    // Comparable implementation (compare by age)
    @Override
    public int compareTo(Employee other) {
        return Integer.compare(this.age, other.age);
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
}

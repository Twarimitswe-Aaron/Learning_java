import java.util.*;

public class CollectionEtude {
    // comparator
    // public static class Student implements Comparable<Student>{
    public static class Student {
        public int age;
        public String name;

        Student(int age, String name) {
            this.age = age;
            this.name = name;
        }

        // @Override
        // public int compareTo(Student o){
        // return this.age-o.age;
        // }
        @Override
        public String toString() {
            return "Name: " + this.name + " Age: " + this.age;
        }

    }

    public static void main(String[] args) {
        List<Student> student = new ArrayList<>();
        student.add(new Student(21, "Alice"));
        student.add(new Student(19, "Bob"));
        student.add(new Student(22, "Charlie"));

        // Collections.sort(student);
        System.out.println("Sorted by age:");
        System.out.println(student);

        // comparator

        Collections.sort(student, new Comparator<Student>() {
            public int compare(Student a, Student b) {
                return a.name.compareTo(b.name);
            }
        });

        System.out.println("Sorted by name: using comparator");
        System.out.println(student);
    }
}
public class ObjectCasting {
    public static void main(String[] args) {
        Employee e= new Employee();
        e.work();
        Employee m=new Manager();
       ((Manager) m).conductMeeting();
                m=(Employee) e;
        m.work();


        Animal a=new Dog();
        Dog d=(Dog) a;
        d.bark();

    }

}

 class Employee {
    void work() {
        System.out.println("Employee works");

    }

}

 class Manager extends Employee {
    void conductMeeting() {
        System.out.println("Manager is conducting meeting");

    }
}

class Animal{

}

class Dog extends  Animal{
    void bark(){
        System.out.println("Dog barks");
    }
}
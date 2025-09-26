public  class SuperKeyword  {
    public static void main(String[] args) {

        Car c=new Car("Benz");
        Student s=new Student();
        s.displayInfo();

    }
}

class Vehicle{
    String vehicle;
    Vehicle(String vehicle){
        this.vehicle=vehicle;
        System.out.println("Vehicle Created");
        System.out.println(vehicle);
    }
}

class Car extends Vehicle{
    Car(String vehicle){
        super(vehicle);
        System.out.println("Car Created");
    }

}

class  Person {
    void displayInfo(){
        System.out.println("This is a Person and is accessed from child using super keyword");
    }
}

class Student extends Person{
    Student(){
        super();
    }



}
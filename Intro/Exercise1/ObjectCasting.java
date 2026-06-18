public class ObjectCasting {
    public static void main(String[] args) {
        CastingEmployee e= new CastingEmployee();
        e.work();
        CastingEmployee m=new CastingManager();
       ((CastingManager) m).conductMeeting();
                m=(CastingEmployee) e;
        m.work();


        Animal a=new Dog();
        Dog d=(Dog) a;
        d.bark();

    }

}

 class CastingEmployee {
    void work() {
        System.out.println("Employee works");

    }

}

 class CastingManager extends CastingEmployee {
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
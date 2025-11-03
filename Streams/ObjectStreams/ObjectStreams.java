package ObjectStreams;

import java.io.*;

class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    String name;
    int age;
    transient int password;

    public Student(String name, int age, int password) {
        this.name = name;
        this.age = age;
        this.password = password;
    }

    @Override

    public String toString() {
        return "Student {name=" + name + ", age=" + age + ", password=" + password + "}";
    }
}

public class ObjectStreams {
    public static void main(String[] args) {
        Student s1=new Student("Aaron",18 ,12);
        System.out.println("The first student is:"+s1);

        //Serialization

        try(ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream("objects.ser"))){
            oos.writeObject(s1);
        }catch(Exception e){
            e.printStackTrace();
        }

        //deserialization

        try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream("objects.ser"))){
            Student s2=(Student)ois.readObject();
            System.out.println("The deserialized student is:"+s2);


        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }


    }
}

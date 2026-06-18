import java.io.*;

public class EmployeeMain
{
    public  static void main(String[] args){
        Employee em=new Employee("Art","zero");
        File file= new File("D:\\Documents\\Notes2\\JAVA\\programs\\This Year\\Streams\\primitives.ser");

        try(ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(file))){
            out.writeObject(em);

        }catch(Exception e){

        }
        try(ObjectInputStream reader=new ObjectInputStream(new FileInputStream(file))) {
            Employee em2=(Employee) reader.readObject();
            System.out.println(em2.getName());
            System.out.println(em2.getSurname());

        }catch (Exception e){

        }
    }
}
class Employee  implements Serializable
{
    private String name;
    private String surname;
    Employee(String name, String surname){
        this.name = name;
        this.surname = surname;

    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSurname() {
        return surname;
    }
}
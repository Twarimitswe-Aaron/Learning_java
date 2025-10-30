interface printable{
    void printDetails();
}

class Computer implements printable{
    public void printDetails(){
        System.out.println("Computer is selected");
    }
}

class Laptop implements printable{
    public void printDetails(){
        System.out.println("Laptop is selected");
    }
}
class Desktop implements printable{
    public void printDetails(){
        System.out.println("Desktop is selected");
    }
}

class PrintObject<T extends printable>{
    public T obj;
    public PrintObject(T obj){
        this.obj=obj;
    }
    public void printDetails(){
        obj.printDetails();
    }
}

public class GenericObject{
    public static void main(String[] args){
        Computer computer=new Computer();
        Laptop laptop=new Laptop();
        Desktop desktop=new Desktop();
        PrintObject<Laptop> printLap=new PrintObject<>(new Laptop());
        printLap.printDetails();
        PrintObject<Desktop> printDesktop=new PrintObject<>(new Desktop());
        printDesktop.printDetails();
        PrintObject<Computer> printComp=new PrintObject<>(new Computer());
        printComp.printDetails();

    }
}
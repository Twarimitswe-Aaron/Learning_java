import java.util.*;
public class PrinterMain {
    public static void main(String[] args) {
        //using arraylist
        ArrayList<Integer> list=new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5456);


        //printing unspecified type but in the Integer type

        Print<Double> printer=new Print<>(123.1);
        printer.print();
        Print<Integer> printer1=new Print<>(123);
        printer1.print();

        //printing String

        PrintAny<String> printer2=new PrintAny<>("Game over");
        printer2.print();

        //Printing arrayList of any type extends Integers

        PrintList<Integer> printList=new PrintList<>(list);
        printList.print();

        PrintTwo<Integer, String> printTwo=new PrintTwo<>(123,"Mama Yake");
        printTwo.print();


    }
}

class PrintAny<A>{
    A a;
     PrintAny(A a){
        this.a=a;
    }
    public void print(){
        System.out.println("Any type value is :"+a);
    }
}

class PrintList<A extends Integer>{
    ArrayList<A> list;
    PrintList(ArrayList<A> list){
        this.list=list;
    }
    public void print(){
        System.out.println("Our list is :"+list);
    }
}

class PrintTwo<A, B>{
    A a;
    B b;
    PrintTwo(A a,B b){
        this.a=a;
        this.b=b;

    }
    public void print(){
        System.out.println("Two type value is :"+a);
        System.out.println("Two type value is :"+b);
    }
}

class Box<T>{
    T obj;
    Box(T obj){
        this.obj=obj;
    }



}
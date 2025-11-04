package Formatting;
import java.text.*;
import java.util.*;



public class PrintFormattingDemo {
    public static void main(String[] args){
        //print numbers on the same line

        System.out.printf("Numbers: %d %d %d %d %d%n",1,2,3,4,5);

        //print pi with 2, 3 foating points

        double PI=Math.PI;
        System.out.printf("PI with 2 decimal points: %.2f%n",PI);
        System.out.printf("PI with 3 decimal points: %.3f%n",PI);
        System.out.printf("PI with 4 decimal points: %.4f%n", PI);

        //left names align and marks right align
        System.out.printf("%-15s %5s%n","Name","Marks");
        System.out.printf("%-15s %5d%n","Alice",85);
        System.out.printf("%-15s %5d%n","Alice",85);
        System.out.printf("%-15s %5d%n","Bob",92);
        System.out.printf("%-15s %5d%n","Charlie",78);


        //display salary with commas

        int salary=1_000_000;
        System.out.printf("Salary: %,d%n",salary);

        //format number for US and French
        double number=1234567.89;
        Locale us=Locale.US;
        Locale fr=Locale.FRANCE;

        System.out.println("US: "+NumberFormat.getInstance(us).format(number));
        System.out.println("France: "+NumberFormat.getInstance(fr).format(number));

        //number in different currency

        double amount=12345.67;
        NumberFormat usCurrency=NumberFormat.getCurrencyInstance(us);
        NumberFormat frCurrency=NumberFormat.getCurrencyInstance(fr);
        NumberFormat jpCurrency=NumberFormat.getCurrencyInstance(Locale.JAPAN);

        System.out.println("US Currency: "+usCurrency.format(amount));
        System.out.println("France Currency: "+frCurrency.format(amount));
        System.out.println("Japan Currency: "+jpCurrency.format(amount));
        

    }
}

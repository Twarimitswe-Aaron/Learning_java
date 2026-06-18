import java.io.*;
import java.util.Scanner;

public class Stream {
    public static void main(String[] args){

//        try(PrintWriter writer=new PrintWriter(new FileWriter(file,true))){
//            writer.println("wakeup..");
//            writer.println("still in java...");
//
//
//        }catch(Exception e){
//            e.printStackTrace();
//        }

        Scanner scan=new Scanner(System.in);
        System.out.println("Enter data , type exit to quit");
        String line;
        while(scan.hasNextLine()){
            line=scan.nextLine();
            if(line.equals("exit")){
                break;
            }
            System.out.println("entered line"+line);
        }
        scan.close();



    }
}


package com.excetions;
import java.util.Scanner;

class Exceptions{
    public static void main(String[] args){
        int x,y,division;
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the value of x");
        x=sc.nextInt();
        System.out.println("Enter the value of y");
        y=sc.nextInt();
        try{
            if(y==0){
                throw new ArithmeticException("avoid zero");
            }
            division=x/y;
            System.out.println("The division is "+division);

        }catch(ArithmeticException e){
            System.out.println("Division by zero");
        }catch (Exception e){
            System.out.println("Exceptions");
            System.out.println(e);
        }
    }

}
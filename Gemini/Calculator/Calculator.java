package Gemini.Calculator;

import java.util.Scanner;

public class Calculator {
    public static void main(String[] args) {
        int a, b, option;
        boolean condition = true;
        Scanner scanner = new Scanner(System.in);

        while (condition) {
            System.out.println("Simple calculator");
            System.out.println("=================");
            System.out.println("1.add");
            System.out.println("2.subtract");
            System.out.println("3.divide");
            System.out.println("0.exit");
            option = scanner.nextInt();

            if (option == 1) {
                System.out.println("Enter the first number");
                a = scanner.nextInt();
                System.out.println("Enter the second number");
                b = scanner.nextInt();
                System.out.println("the sum of " + a + " and " + b + " is " + (a + b));

            } else if (option == 2) {
                System.out.println("Enter the first number");
                a = scanner.nextInt();
                System.out.println("Enter the second number");
                b = scanner.nextInt();
                System.out.println("the difference between " + a + " and " + b + " is " + (a + b));

            } else if (option == 0) {
                condition = false;

            } else if (option == 3) {

            } else {
                System.out.println("Inter a valid option");
            }
            System.out.println();

        }

    }

}
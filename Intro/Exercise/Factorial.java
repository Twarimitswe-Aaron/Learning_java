public class Factorial {
    public static void main(String[] args) {
        int factorial = 10;
        int product=1;
        for (int i = factorial; i >0; i--) {
            product *= i;
        }
        System.out.println("The factorial of 10 is " + product);
    }
}

package OOP.Inheritance.Interface;

public class MultiplierMain {
    public static void main(String[] args) {
        Multiplier mult = (a, b) -> a * b;
        int k = mult.multiply(5, 4);
        System.out.println(k);

    }
}

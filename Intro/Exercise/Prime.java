
public class Prime {

    public static void main(String[] args) {
        int target = 50;
        for (int i = target - 1; i > 0; i--) {
            if (target % i == 0) {
                System.out.println(target + " is not a prime number because it is divisible by " + i);
                break;
            } else if (i == 1) {
                System.out.println(target + " is a prime number");
                break;
            }
        }
    }
}

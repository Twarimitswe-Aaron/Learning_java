import java.util.Scanner;

class Delimiter2{
    public static void main(String []a){
        String data = "Honorine ntago yasinziriye 5 turamuha bonbon";
        String data2 = "17/10/2025";

        // --- Part 1: Extract name and bonbon count ---
        Scanner scan = new Scanner(data);

        String token1 = scan.next(); // Honorine
        String token2 = scan.next(); // ntago
        scan.next(); // skip "yasinziriye"
        int token4 = scan.nextInt(); // 5

        System.out.println("Name: " + token1 + " Bonbon: " + token4);

        // --- Part 2: Extract and sum date parts ---
        Scanner dateScanner = new Scanner(data2);
        dateScanner.useDelimiter("/"); // use '/' as delimiter

        int day = dateScanner.nextInt();    // 17
        int month = dateScanner.nextInt();  // 10
        int year = dateScanner.nextInt();   // 2025

        int sum = day + month + year;

        System.out.println("Date: " + day + "/" + month + "/" + year);
        System.out.println("Sum of date parts: " + sum);

        // Close scanners
        scan.close();
        dateScanner.close();

    }
}
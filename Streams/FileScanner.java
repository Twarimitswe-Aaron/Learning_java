import java.io.*;

public class FileScanner {
    public static void main(String[] args) {
        try (java.util.Scanner s = new java.util.Scanner(new FileInputStream("stream.txt"))) {
            while (s.hasNextLine()) {
                System.out.println(s.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

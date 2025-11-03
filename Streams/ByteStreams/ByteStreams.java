import java.io.*;

class ByteStreams {
    public static void main(String[] args) {
        try (FileOutputStream fos = new FileOutputStream("example.txt");
                FileInputStream fis = new FileInputStream("example.txt");) {
            // we use fileOUtputStream to put data from our program to file
            String data = "Hello , I am learning about streams of files in Java";
            fos.write(data.getBytes());
            int i;

            // we use fileInput streams to insert data from file to out program
            while ((i = fis.read()) != -1) {
                System.out.print((char) i);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
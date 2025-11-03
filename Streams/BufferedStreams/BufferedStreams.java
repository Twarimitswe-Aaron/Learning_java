package BufferedStreams;
import java.io.*;


public class BufferedStreams {
    public static void main(String[] args) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("file.txt"));
                BufferedReader br = new BufferedReader(new FileReader("file.txt"));) {
            String data = "Hello i am learning buffered streams";
            bw.write(data);
            int i;
            while ((i = br.read()) != -1) {
                System.out.println((char) i);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}

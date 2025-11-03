package CharacterStream;

import java.io.*;

public class CharacterStream {
    public static void main(String[] args) {
        File file = new File("file.txt");
        try {
            if (!file.exists()) {
                System.out.println("File not found, creating new file");
                if (file.createNewFile()) {
                    System.out.println("File created successfully");
                } else {
                    System.out.println("Failed to create file");
                }
            }
            FileWriter writer = new FileWriter(file);
            FileReader reader = new FileReader(file);

            String data = "Hello, I am learning about character streams in Java.";
            writer.write(data);
            writer.close();
            int i;
            while ((i = reader.read()) != -1) {
                System.out.print((char) i);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

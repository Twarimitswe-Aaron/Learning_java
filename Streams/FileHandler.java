import java.io.*;

public class FileHandler {
    public static void main(String[] args) {
        String data = "hello RCA students!!";
        try (FileOutputStream out = new FileOutputStream(
                "/run/media/twarimitswe_aaron/New Volume/Documents/Notes2/JAVA/programs/This Year/Streams/source.txt")) {
            byte[] bytes = data.getBytes();
            out.write(bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileInputStream in = new FileInputStream(
                "/run/media/twarimitswe_aaron/New Volume/Documents/Notes2/JAVA/programs/This Year/Streams/source.txt")) {
            int content;
            while ((content = in.read()) != -1) {
                System.out.print((char) content);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        // using file reader to read file directly without converting to bytes

        String anotherContent = "I am working with remaining stuffs";

        try (FileWriter writer = new FileWriter(
                "/run/media/twarimitswe_aaron/New Volume/Documents/Notes2/JAVA/programs/This Year/Streams/source.txt")) {
            writer.write(anotherContent);

        } catch (Exception e) {
            e.printStackTrace();

        }

        try (FileReader reader = new FileReader(
                "/run/media/twarimitswe_aaron/New Volume/Documents/Notes2/JAVA/programs/This Year/Streams/source.txt")) {
            int character;
            System.out.println();

            System.out.println("Reading Whole file one character by one");
            while ((character = reader.read()) != -1) {
                System.out.print((char) character);
            }
        } catch (Exception e) {

            e.printStackTrace();
        }


        

    }

}
import java.io.*;

public class IOStream {
    public static void main(String[] args) {
        String str = "Joshua ari gusakuza";
        int content;

        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("output.txt"));
                BufferedInputStream in = new BufferedInputStream(new FileInputStream("output.txt"))) {

            out.write(str.getBytes());
            out.flush();

            while ((content = in.read()) != -1) {
                System.out.print((char) content);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader br = new BufferedReader(new FileReader("output.txt"));
                BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt", true))) {
            System.out.println(br.readLine());
            bw.newLine();
            bw.write("I am learning Java IO Streams");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream("dataoutput.txt"));
                DataInputStream dis = new DataInputStream(new FileInputStream("dataoutput.txt"))) {
            dos.writeUTF("Hello Data Streams");
            dos.writeInt(2024);
            dos.flush();

            System.out.println(dis.readUTF());
            System.out.println(dis.readInt());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
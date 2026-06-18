import java.io.*;

public class Primitives {
    public  static void  main(String[] args){
        File file=new File("D:\\Documents\\Notes2\\JAVA\\programs\\This Year\\Streams\\primitives.dat");
        try(DataOutputStream dos=new DataOutputStream(new FileOutputStream(file))) {
            dos.writeInt(5);
            dos.writeUTF("hello");
            dos.writeBoolean(true);

        }catch (Exception e){
            e.printStackTrace();
        }
        try(DataInputStream dis=new DataInputStream(new FileInputStream(file))) {
            System.out.println(dis.readInt());
            System.out.println(dis.readUTF());
            System.out.println(dis.readBoolean());

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
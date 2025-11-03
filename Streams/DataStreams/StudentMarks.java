
import java.io.*;

public class StudentMarks {
    public static void main(String[] args) {
        String fileName = "studentData.txt";

        // try inserting values inside the file using dataoutpustream

        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(fileName))) {
            dos.writeUTF("Aaron");
            dos.writeInt(1);
            dos.writeDouble(1.2);
            dos.writeBoolean(true);

            dos.writeUTF("Arnaud");
            dos.writeInt(2);
            dos.writeDouble(1.3);
            dos.writeBoolean(true);

            dos.writeUTF("Brenda");
            dos.writeInt(3);
            dos.writeDouble(1.4);
            dos.writeBoolean(false);

            dos.writeUTF("challom");
            dos.writeInt(4);
            dos.writeDouble(1.2);
            dos.writeBoolean(true);

            System.out.println("Data written successfully.");

        } catch (Exception e) {
            System.out.println("Error writing data: " + e.getMessage());
        }

    
        // try reading values from the file using datainputstream
        try (DataInputStream dis=new DataInputStream(new FileInputStream(fileName))) {
           for(int i=0;dis.available()>0;i++){
            String name=dis.readUTF();
            int rollNo=dis.readInt();
            double marks =dis.readDouble();
            boolean passed=dis.readBoolean();

            System.out.println("Student "+(i+1)+": Name="+name+", Roll No="+rollNo+", Marks="+marks+", Passed="+passed);
           }
            
        } catch (Exception e) {
            System.out.println("Error reading data: " + e.getMessage());
        }
    }
}
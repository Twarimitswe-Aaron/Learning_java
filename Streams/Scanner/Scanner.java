import java.io.*;

public class Scanner {
    public static void main(String[] args){
        try {
            File file=new File("../CharacterStream/file.txt");
            if(!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    System.out.println("Error creating file: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            java.util.Scanner sc=new java.util.Scanner(file);
            while(sc.hasNextInt()){
                System.out.println(sc.nextInt());
            }
            sc.close();
        } catch (FileNotFoundException ex) {
        }

    }
}

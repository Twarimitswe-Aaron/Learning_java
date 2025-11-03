package Scanner;

import java.io.*;

public class Scanner {
    public static void main(String[] args){
        try {
            File file=new File("../CharacterStream/file.txt");
            if(!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
            java.util.Scanner sc=new java.util.Scanner(file);
            while(sc.hasNextInt()){
                System.out.println(sc.nextInt());
            }
        } catch (FileNotFoundException ex) {
        }

    }
}

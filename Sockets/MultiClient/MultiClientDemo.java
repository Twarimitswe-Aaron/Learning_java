package MultiClient;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class MultiClientDemo {
    public static void main(String[] args) {
        // Connect to the multi-server on localhost port 6000
        try (Socket socket = new Socket("localhost", 6000)) {
            System.out.println("Connected to the multi-client server!");

            // Setup I/O streams and Scanner for user input
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in);

            // Read the welcome message from the server
            System.out.println(input.readLine());

            // Loop to continuously send messages
            while (true) {
                System.out.print("Enter message: ");
                String message = scanner.nextLine();
                
                // Send message to server
                output.println(message);

                // Exit if the user types 'exit'
                if (message.equalsIgnoreCase("exit")) {
                    break;
                }

                // Read the server's echo response
                String response = input.readLine();
                System.out.println(response);
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("Failed to connect. Is the multi-client server running?");
            e.printStackTrace();
        }
    }
}

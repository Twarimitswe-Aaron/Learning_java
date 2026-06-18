package SingleClient;


import java.io.*;
import java.net.*;


public class Client {
    public static void main(String[] args) {
        // Connect to the server on localhost port 5000
        try (Socket socket = new Socket("localhost", 5000)) {
            System.out.println("Connected to the server!");

            // Setup input and output streams for communication
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Send a message to the server
            output.println("Hello Server, this is the single client!");
            
            // Read the response from the server
            String serverResponse = input.readLine();
            System.out.println("Server says: " + serverResponse);

        } catch (IOException e) {
            System.out.println("Failed to connect. Is the server running?");
            e.printStackTrace();
        }
    }
}

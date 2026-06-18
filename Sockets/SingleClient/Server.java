import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        // Create a ServerSocket listening on port 5000
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server started. Waiting for a client on port 5000...");
            
            // Accept the incoming connection (this is a blocking call)
            Socket socket = serverSocket.accept();
            System.out.println("Client connected!");

            // Setup input and output streams for communication
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            // Read the message from the client
            String clientMessage = input.readLine();
            System.out.println("Client says: " + clientMessage);

            // Send a response back to the client
            output.println("Hello from the server! I received your message.");
            
            // Close the socket connection
            socket.close();
            System.out.println("Connection closed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

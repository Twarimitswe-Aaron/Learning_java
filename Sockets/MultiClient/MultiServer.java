import java.io.*;
import java.net.*;

public class MultiServer {
    public static void main(String[] args) {
        // Create a ServerSocket listening on port 6000
        try (ServerSocket serverSocket = new ServerSocket(6000)) {
            System.out.println("Multi-Client Server started on port 6000...");

            // Infinite loop to continuously accept new clients
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket.getInetAddress().getHostAddress());
                
                // Spawn a new Thread to handle the client asynchronously
                // This allows the server to go back to accepting other clients
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// A custom Thread class to handle individual client connections
class ClientHandler extends Thread {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
        ) {
            // Send a welcome message to the newly connected client
            output.println("Welcome to the Multi-Client Server! Type 'exit' to disconnect.");
            
            String message;
            // Continuously listen for messages from this specific client
            while ((message = input.readLine()) != null) {
                System.out.println("Received from client: " + message);
                
                // Disconnect if the client sends 'exit'
                if (message.equalsIgnoreCase("exit")) {
                    break;
                }
                
                // Echo the message back to the client
                output.println("Server echo: " + message);
            }
        } catch (IOException e) {
            System.out.println("Client disconnected unexpectedly.");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Connection with client closed.");
        }
    }
}

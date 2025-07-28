import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private ChatServer server; // Reference to the main server to broadcast messages
    private PrintWriter writer;
    private BufferedReader reader;
    private String userName;

    public ClientHandler(Socket socket, ChatServer server) {
        this.clientSocket = socket;
        this.server = server;
        try {
            // Get input and output streams for communication with this client
            this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.writer = new PrintWriter(clientSocket.getOutputStream(), true); // 'true' for auto-flush
        } catch (IOException e) {
            System.err.println("Error setting up client handler: " + e.getMessage());
            closeResources();
        }
    }

    @Override
    public void run() {
        try {
            // 1. Get username from client
            writer.println("Enter your username:");
            userName = reader.readLine();
            if (userName == null || userName.trim().isEmpty()) {
                userName = "Guest" + clientSocket.getPort(); // Fallback
            }
            System.out.println(userName + " has connected.");
            server.broadcastMessage(userName + " has joined the chat.", this); // Notify others

            String message;
            // 2. Continuously read messages from this client
            while ((message = reader.readLine()) != null) {
                if (message.equalsIgnoreCase("exit")) {
                    break; // Client wants to disconnect
                }
                // 3. Broadcast the message to all other clients
                server.broadcastMessage(userName + ": " + message, this);
            }

        } catch (IOException e) {
            // Handle client disconnection or other I/O errors
            if (userName != null) {
                System.err.println(userName + " disconnected unexpectedly: " + e.getMessage());
            } else {
                System.err.println("A client disconnected unexpectedly: " + e.getMessage());
            }
        } finally {
            // 4. Clean up: remove client from list and close resources
            if (userName != null) {
                server.removeClient(this); // Remove from server's list
                server.broadcastMessage(userName + " has left the chat.", this); // Notify others
                System.out.println(userName + " has left the chat.");
            }
            closeResources();
        }
    }

    // Method to send a message *to this specific client*
    public void sendMessage(String message) {
        writer.println(message);
    }

    // Helper method to close all resources
    private void closeResources() {
        try {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (clientSocket != null && !clientSocket.isClosed()) clientSocket.close();
        } catch (IOException e) {
            System.err.println("Error closing client resources: " + e.getMessage());
        }
    }
}

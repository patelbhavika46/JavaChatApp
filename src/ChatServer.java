import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatServer {
    private static final int PORT = 12345; // Port for the server to listen on
    private ServerSocket serverSocket;
    // Use a synchronized list to safely manage client handlers across threads
    private List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());

    public ChatServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Chat server started on port " + port);
        } catch (IOException e) {
            System.err.println("Could not listen on port " + port + ": " + e.getMessage());
            System.exit(1);
        }
    }

    public void startServer() {
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept(); // Blocks until a client connects
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());

                // Create a new handler for this client and start it in a new thread
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clients.add(clientHandler); // Add to our list of active clients
                new Thread(clientHandler).start(); // Start the client's communication thread
            }
        } catch (IOException e) {
            System.err.println("Error accepting client connection: " + e.getMessage());
        } finally {
            stopServer();
        }
    }

    // Method to send a message to all clients except the sender
    public void broadcastMessage(String message, ClientHandler sender) {
        // Synchronize access to the clients list to prevent ConcurrentModificationException
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client != sender) { // Don't send the message back to the sender
                    client.sendMessage(message);
                }
            }
        }
    }

    // Method to remove a client from the list (called when a client disconnects)
    public void removeClient(ClientHandler clientToRemove) {
        clients.remove(clientToRemove);
    }

    // Method to stop the server and close all client connections
    public void stopServer() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("Server stopped.");
            }
            // Close all active client connections
            synchronized (clients) {
                for (ClientHandler client : clients) {
                    // For simplicity, we'll let the ClientHandler's finally block handle its own closing.
                    // In a more robust app, you might explicitly call a client.close() method here.
                }
                clients.clear();
            }
        } catch (IOException e) {
            System.err.println("Error stopping server: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ChatServer server = new ChatServer(PORT);
        server.startServer();
    }
}

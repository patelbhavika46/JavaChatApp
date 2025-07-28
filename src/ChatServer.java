import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ChatServer accepts multiple ChatClients and relays messages between them.
 * Each client connection is handled in a dedicated thread.
 */
public class ChatServer {
    public static final int PORT = 12345;
    private ServerSocket serverSocket;
    // CopyOnWriteArrayList is thread-safe for add/remove and iteration
    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private volatile boolean running = true;

    public ChatServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Chat server started on port " + port);
        } catch (IOException e) {
            System.err.println("Could not listen on port " + port + ": " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Starts accepting client connections.
     */
    public void startServer() {
        try {
            while (running) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            if (running) {
                System.err.println("Error accepting client connection: " + e.getMessage());
            }
        } finally {
            stopServer();
        }
    }

    /**
     * Broadcasts a message to all clients except the sender.
     */
    public void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    /**
     * Removes a client from the list (when disconnected).
     */
    public void removeClient(ClientHandler clientToRemove) {
        clients.remove(clientToRemove);
    }

    /**
     * Gracefully stops the server and closes all resources.
     */
    public void stopServer() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) serverSocket.close();
            for (ClientHandler client : clients) {
                client.closeResources();
            }
            clients.clear();
            System.out.println("Server stopped.");
        } catch (IOException e) {
            System.err.println("Error stopping server: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ChatServer server = new ChatServer(PORT);
        server.startServer();
    }
}
import java.io.*;
import java.net.Socket;

/**
 * ClientHandler manages communication with a single ChatClient.
 * Each handler runs on its own thread and relays messages to the ChatServer.
 */
public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final ChatServer server;
    private PrintWriter writer;
    private BufferedReader reader;
    private String userName;
    private volatile boolean running = true;

    public ClientHandler(Socket socket, ChatServer server) {
        this.clientSocket = socket;
        this.server = server;
        try {
            this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.writer = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println("Error setting up client handler: " + e.getMessage());
            closeResources();
        }
    }

    @Override
    public void run() {
        try {
            // Prompt for username
            writer.println("Enter your username:");
            userName = reader.readLine();
            if (userName == null || userName.trim().isEmpty()) {
                userName = "Guest" + clientSocket.getPort();
            }
            System.out.println(userName + " has connected.");
            server.broadcastMessage(userName + " has joined the chat.", this);

            String message;
            while (running && (message = reader.readLine()) != null) {
                if (message.equalsIgnoreCase("exit")) {
                    break;
                }
                if (!message.trim().isEmpty()) {
                    server.broadcastMessage(userName + ": " + message, this);
                }
            }
        } catch (IOException e) {
            System.err.println((userName != null ? userName : "A client") + " disconnected unexpectedly: " + e.getMessage());
        } finally {
            running = false;
            server.removeClient(this);
            server.broadcastMessage(userName + " has left the chat.", this);
            System.out.println(userName + " has left the chat.");
            closeResources();
        }
    }

    /**
     * Sends a message to this client.
     */
    public void sendMessage(String message) {
        writer.println(message);
    }

    /**
     * Closes all I/O streams and the client socket.
     */
    public void closeResources() {
        try {
            if (reader != null) reader.close();
        } catch (IOException ignored) {}
        try {
            if (writer != null) writer.close();
        } catch (Exception ignored) {}
        try {
            if (clientSocket != null && !clientSocket.isClosed()) clientSocket.close();
        } catch (IOException ignored) {}
    }
}
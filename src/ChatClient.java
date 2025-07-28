import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * ChatClient connects to a ChatServer and allows the user to send and receive messages via console.
 * Supports graceful disconnection and concurrent message receiving.
 */
public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private Scanner scanner;
    private String userName;
    private volatile boolean running = true;

    public ChatClient() {
        scanner = new Scanner(System.in);
    }

    public void startClient() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected to the chat server.");

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            // Username prompt is handled by the server, but we keep our username for local reference
            System.out.print("Enter your username: ");
            userName = scanner.nextLine().trim();
            if (userName.isEmpty()) {
                userName = "Guest" + socket.getLocalPort();
            }
            writer.println(userName);

            // Listen for server messages in a separate thread
            Thread listenerThread = new Thread(new ServerMessageListener());
            listenerThread.start();

            // Main thread: send user input to server
            while (running) {
                String userInput = scanner.nextLine();
                if (userInput.equalsIgnoreCase("exit")) {
                    writer.println("exit");
                    running = false;
                    break;
                }
                writer.println(userInput);
            }
            listenerThread.join();
        } catch (IOException | InterruptedException e) {
            System.err.println("Connection error: " + e.getMessage());
        } finally {
            closeResources();
        }
    }

    /**
     * Listens for server messages and prints them to the console.
     */
    private class ServerMessageListener implements Runnable {
        @Override
        public void run() {
            try {
                String serverMessage;
                while ((serverMessage = reader.readLine()) != null) {
                    System.out.println(serverMessage);
                }
            } catch (IOException e) {
                if (running) {
                    System.err.println("Disconnected from server: " + e.getMessage());
                }
            } finally {
                running = false;
            }
        }
    }

    /**
     * Properly closes all resources and notifies the user.
     */
    private void closeResources() {
        try {
            if (scanner != null) scanner.close(); // System.in, only close once per JVM
        } catch (Exception ignored) {}
        try {
            if (reader != null) reader.close();
        } catch (IOException ignored) {}
        try {
            if (writer != null) writer.close();
        } catch (Exception ignored) {}
        try {
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException ignored) {}
        System.out.println("Disconnected from chat.");
    }

    public static void main(String[] args) {
        new ChatClient().startClient();
    }
}
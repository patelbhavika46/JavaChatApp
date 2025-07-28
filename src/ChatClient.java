import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost"; // Server IP address
    private static final int SERVER_PORT = 12345; // Server port

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private Scanner scanner;
    private String userName;

    public ChatClient() {
        scanner = new Scanner(System.in);
    }

    public void startClient() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected to the chat server.");

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true); // Auto-flush

            // 1. Get username
            // The server will also prompt, so this first prompt is just for the client's internal tracking
            System.out.print("Enter your username: ");
            userName = scanner.nextLine();
            writer.println(userName); // Send username to server

            // 2. Start a separate thread to listen for server messages
            new Thread(new ServerMessageListener()).start();

            // 3. Main thread: read user input and send to server
            String userInput;
            while (true) {
                userInput = scanner.nextLine();
                if (userInput.equalsIgnoreCase("exit")) {
                    writer.println("exit"); // Tell server we're leaving
                    break;
                }
                writer.println(userInput); // Send user's message to server
            }

        } catch (IOException e) {
            System.err.println("Error connecting to server or during chat: " + e.getMessage());
        } finally {
            closeResources();
        }
    }

    // Inner class (or separate class) to listen for messages from the server
    private class ServerMessageListener implements Runnable {
        @Override
        public void run() {
            try {
                String serverMessage;
                while ((serverMessage = reader.readLine()) != null) {
                    System.out.println(serverMessage);
                }
            } catch (IOException e) {
                // This exception usually means the server disconnected or socket closed
                System.err.println("Disconnected from server: " + e.getMessage());
            } finally {
                // Resources are closed by the main thread's finally block
            }
        }
    }

    // Helper method to close client resources
    private void closeResources() {
        try {
            if (scanner != null) scanner.close();
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (socket != null && !socket.isClosed()) socket.close();
            System.out.println("Disconnected from chat.");
        } catch (IOException e) {
            System.err.println("Error closing client resources: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ChatClient client = new ChatClient();
        client.startClient();
    }
}

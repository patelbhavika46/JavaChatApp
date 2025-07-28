# Basic Java Console Chat Application

This is a simple client-server chat application developed in Java, demonstrating fundamental concepts of networking (sockets) and multithreading.

---

## Features

- **Client-Server Architecture:** A central server relays messages between multiple connected clients.
- **Multithreaded Server:** The server handles each client connection in a separate thread, allowing concurrent communication.
- **Multithreaded Client:** The client can simultaneously send messages (from user input) and receive messages (from the server).
- **Console-Based:** All interactions happen via the command line.
- **Basic Usernames:** Clients provide a username upon connecting.
- **Broadcast Messages:** Messages from one client are broadcast to all other active clients.

---

## Java Concepts Demonstrated

- **Networking:** `java.net.ServerSocket` and `java.net.Socket` for establishing connections.
- **Multithreading:** `java.lang.Thread` and `java.lang.Runnable` for concurrent execution.
- **Input/Output Streams:** `java.io.BufferedReader`, `java.io.PrintWriter`, `java.io.InputStreamReader`, `java.io.OutputStreamWriter` for data transfer.
- **Concurrency Utilities:** `java.util.Collections.synchronizedList` for thread-safe management of connected clients.
- **Error Handling:** `try-catch-finally` blocks for robust network operations and resource management.

---

## How to Run

### Prerequisites

- Java Development Kit (JDK) 8 or higher installed.

### Steps

1. **Clone or Download**
    - Download or clone this repository to your local machine.

2. **Navigate to Project Directory**
    - Open your terminal or command prompt and navigate to the `JavaChatApp` directory (the one containing the `src` folder).

3. **Compile the Java Files**

   ```sh
   javac src/ChatServer.java src/ClientHandler.java src/ChatClient.java
   ```

   > *If you're using an IDE like IntelliJ IDEA or Eclipse, compilation is usually handled automatically.*

4. **Start the Server**

    - Open a new terminal/command prompt window and run:

      ```sh
      java -cp src ChatServer
      ```

    - You should see the message:  
      `Chat server started on port 12345`

5. **Start Clients**

    - Open one or more additional terminal/command prompt windows (each for a separate client) and run in each:

      ```sh
      java -cp src ChatClient
      ```

    - Each client will prompt you to enter a username.
    - Type your messages and press Enter. You should see them appear in other client windows and on the server console.
    - To disconnect a client, type `exit` and press Enter.

---

## Project Structure

```
JavaChatApp/
├── src/
│   ├── ChatServer.java       # Main server logic
│   ├── ClientHandler.java    # Handles individual client connections (runs in a separate thread)
│   └── ChatClient.java       # Client application logic
└── README.md                 # This file
```

---

## License

This project is provided for educational purposes and may be freely used, modified, and distributed.

---
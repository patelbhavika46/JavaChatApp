# Basic Java Console Chat Application

This is a simple, robust client-server chat application developed in Java, demonstrating core networking (sockets), multithreading, and clean architecture principles.  
It is designed for readability, extensibility, and correctness, and now includes guidance for unit testing and improved project organization.

---

## Features

- **Client-Server Architecture:** A central server relays messages between multiple connected clients.
- **Multithreaded Server:** The server handles each client connection in a separate thread, allowing concurrent communication.
- **Multithreaded Client:** The client can simultaneously send messages (from user input) and receive messages (from the server).
- **Console-Based:** All interactions happen via the command line.
- **Usernames:** Clients provide a username upon connecting (with fallback to a guest name if blank).
- **Broadcast Messages:** Messages from one client are broadcast to all other active clients.
- **Proper Resource Management:** All network and I/O resources are closed gracefully.
- **Exception Handling:** Robust error management for all common scenarios and abrupt disconnects.
- **Extensible and Testable:** Logic is organized for easy unit testing and further feature extension.
- **Unit Tests Included:** Example JUnit test file for protocol logic and edge cases.
- **Clean Project Structure:** Supports compiling `.class` files into an `out` directory for separation of source and binaries.

---

## Java Concepts Demonstrated

- **Networking:** `java.net.ServerSocket` and `java.net.Socket` for establishing connections.
- **Multithreading:** `java.lang.Thread`, `java.lang.Runnable` for concurrent execution.
- **Input/Output Streams:** `java.io.BufferedReader`, `java.io.PrintWriter`, `java.io.InputStreamReader`, `java.io.OutputStreamWriter` for data transfer.
- **Thread-Safety:** `java.util.concurrent.CopyOnWriteArrayList` for managing client connections.
- **Error Handling:** `try-catch-finally` blocks for robust network operations and resource management.
- **Unit Testing:** JUnit 5 test file for protocol logic.

---

## How to Run

### Prerequisites

- Java Development Kit (JDK) 8 or higher installed.

### Steps

1. **Clone or Download**
   - Download or clone this repository to your local machine.

2. **Navigate to Project Directory**
   - Open your terminal or command prompt and navigate to the `JavaChatApp` directory (the one containing the `src` folder).

3. **Compile the Java Files to an Output Directory**

   First, create the output directory:

   ```sh
   mkdir out
   ```

   Then compile all Java files to `out`:

   ```sh
   javac -d out src/*.java
   ```

   > *If you're using an IDE like IntelliJ IDEA or Eclipse, compilation and output directory setup can be handled automatically.*

4. **Start the Server**

   - Open a new terminal/command prompt window and run:

     ```sh
     java -cp out ChatServer
     ```

   - You should see the message:  
     `Chat server started on port 12345`

5. **Start Clients**

   - Open one or more additional terminal/command prompt windows (each for a separate client) and run in each:

     ```sh
     java -cp out ChatClient
     ```

   - Each client will prompt you to enter a username.
   - Type your messages and press Enter. Messages will be shown in all connected client windows and on the server console.
   - To disconnect a client, type `exit` and press Enter.

### How to Run Unit Tests

1. **(Optional) Download JUnit Platform Console Standalone JAR**  
   Download [junit-platform-console-standalone-1.9.3.jar](https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.9.3/junit-platform-console-standalone-1.9.3.jar) and place it in a `lib` directory at your project root.

2. **Compile Test File to `out` Directory**

   ```sh
   javac -cp .;lib/junit-platform-console-standalone-1.9.3.jar;out -d out src/ChatProtocolTest.java
   ```

3. **Run the Tests**

   ```sh
   java -jar lib/junit-platform-console-standalone-1.9.3.jar -cp out --scan-class-path
   ```

   You should see test discovery and results in your terminal.

---

## Project Structure

```
JavaChatApp/
├── src/
│   ├── ChatServer.java         # Main server logic
│   ├── ClientHandler.java      # Handles individual client connections (runs in a separate thread)
│   ├── ChatClient.java         # Client application logic
│   └── ChatProtocolTest.java   # (Optional) Unit tests for protocol logic
├── out/                        # Compiled .class files (after compilation)
├── lib/
│   └── junit-platform-console-standalone-1.9.3.jar   # (Optional, for testing)
└── README.md
```

---

## Extending This Project

- **Private Messages:** Add command parsing to ClientHandler and ChatClient for `/w user message` style whispers.
- **Web Support:** Replace socket logic with WebSocket or REST API for browser-based chat.
- **Logging:** Add server logs to track connections and messages.
- **GUI:** Use JavaFX or Swing for a graphical chat client.
- **Authentication:** Require passwords/user management for connecting clients.

---

## License

This project is provided for educational purposes and may be freely used, modified, and distributed.

---
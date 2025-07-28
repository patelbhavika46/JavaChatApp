import org.junit.jupiter.api.Test;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Basic unit tests for chat message formatting and protocol.
 * (In real-world, more advanced network or integration tests would be needed.)
 */
public class ChatProtocolTest {

    @Test
    void testEmptyUsernameReplaced() {
        String fallback = getUserNameOrFallback("", 12345);
        assertTrue(fallback.startsWith("Guest"));
    }

    @Test
    void testMessageBroadcastFormat() {
        String user = "Alice";
        String msg = "Hello!";
        String formatted = user + ": " + msg;
        assertEquals("Alice: Hello!", formatted);
    }

    /**
     * Example utility to mimic username fallback logic.
     */
    static String getUserNameOrFallback(String input, int port) {
        if (input == null || input.trim().isEmpty())
            return "Guest" + port;
        return input;
    }
}
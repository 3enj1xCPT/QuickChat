
package com.benjamin.quickchat;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    private static final String VALID_RECIPIENT = "+27813051715";

    @Test
    public void testMessageIDHasTenDigits() {
        Message message = new Message(VALID_RECIPIENT, "Hello");
        assertTrue(message.checkMessageID());
        assertTrue(message.getMessageID().matches("\\d{10}"));
    }

    @Test
    public void testMessageNumbersIncrement() {
        Message first = new Message(VALID_RECIPIENT, "First");
        Message second = new Message(VALID_RECIPIENT, "Second");

        assertEquals(
                first.getMessageNumber() + 1,
                second.getMessageNumber()
        );
    }

    @Test
    public void testValidRecipient() {
        Message message = new Message(
                VALID_RECIPIENT, "Hello"
        );

        assertEquals(
                "Cell phone number successfully captured.",
                message.checkRecipientCell()
        );
    }

    @Test
    public void testInvalidRecipient() {
        Message message = new Message(
                "0813051715", "Hello"
        );

        assertFalse(
                message.checkRecipientCell().equals(
                        "Cell phone number successfully captured."
                )
        );
    }

    @Test
    public void testMessageWithinLengthLimit() {
        Message message = new Message(
                VALID_RECIPIENT, "Hello there"
        );

        assertEquals(
                "Message ready to send.",
                message.checkMessageLength()
        );
    }

    @Test
    public void testMessageExactly250Characters() {
        Message message = new Message(
                VALID_RECIPIENT, "A".repeat(250)
        );

        assertEquals(
                "Message ready to send.",
                message.checkMessageLength()
        );
    }

    @Test
    public void testMessageExceeds250Characters() {
        Message message = new Message(
                VALID_RECIPIENT, "A".repeat(251)
        );

        assertTrue(
                message.checkMessageLength().contains(
                        "exceeds 250 characters by 1"
                )
        );
    }

    @Test
    public void testEmptyMessageRejected() {
        Message message = new Message(
                VALID_RECIPIENT, ""
        );

        assertEquals(
                "Please enter a message.",
                message.checkMessageLength()
        );
    }

    @Test
    public void testMessageHashFormat() {
        Message message = new Message(
                VALID_RECIPIENT, "Hi Mike tonight"
        );

        String expected =
                message.getMessageID().substring(0, 2)
                + ":" + message.getMessageNumber()
                + ":HITONIGHT";

        assertEquals(
                expected,
                message.getMessageHash()
        );
    }

    @Test
    public void testSendMessage() {
        Message message = new Message(
                VALID_RECIPIENT, "Hello"
        );

        int previousTotal = Message.returnTotalMessagess();

        assertEquals(
                "Message successfully sent.",
                message.SentMessage(1)
        );

        assertEquals("Sent", message.getStatus());

        assertEquals(
                previousTotal + 1,
                Message.returnTotalMessagess()
        );
    }

    @Test
    public void testDisregardMessage() {
        Message message = new Message(
                VALID_RECIPIENT, "Delete this"
        );

        assertEquals(
                "Press 0 to delete the message.",
                message.SentMessage(2)
        );

        assertEquals(
                "Message disregarded.",
                message.disregardMessage(0)
        );

        assertEquals(
                "Disregarded",
                message.getStatus()
        );
    }

    @Test
    public void testInvalidAction() {
        Message message = new Message(
                VALID_RECIPIENT, "Hello"
        );

        assertEquals(
                "Invalid option.",
                message.SentMessage(9)
        );
    }

    @Test
    public void testCannotSendSameMessageTwice() {
        Message message = new Message(
                VALID_RECIPIENT, "Hello"
        );

        assertEquals(
                "Message successfully sent.",
                message.SentMessage(1)
        );

        assertEquals(
                "Message has already been processed.",
                message.SentMessage(1)
        );
    }

    @Test
    public void testSentMessagesAppearInSummary() {
        Message message = new Message(
                VALID_RECIPIENT, "Unique test message"
        );

        message.SentMessage(1);

        String summary = Message.printMessages();

        assertTrue(summary.contains(message.getMessageID()));
        assertTrue(summary.contains("Unique test message"));
        assertTrue(summary.contains(VALID_RECIPIENT));
    }
}

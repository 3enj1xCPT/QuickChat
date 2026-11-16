
package com.benjamin.quickchat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Part3MessageTest {

    private static final Path JSON_FILE =
            Paths.get("stored_messages.json");

    private boolean fileExisted;
    private byte[] originalFile;

    // Prepare a clean JSON file before each test
    @BeforeEach
    public void setUp() throws IOException {

        fileExisted = Files.exists(JSON_FILE);

        if (fileExisted) {
            originalFile = Files.readAllBytes(JSON_FILE);
        }

        Files.writeString(
                JSON_FILE,
                "[]",
                StandardCharsets.UTF_8
        );

        Message.loadStoredMessages();
    }

    // Restore the original JSON file after each test
    @AfterEach
    public void tearDown() throws IOException {

        if (fileExisted) {
            Files.write(JSON_FILE, originalFile);
        } else {
            Files.deleteIfExists(JSON_FILE);
        }

        Message.loadStoredMessages();
    }

    // TEST 1: Verify that sent messages are recorded
    @Test
    public void testSentMessageCollection() {

        Message message = new Message(
                "+27834557896",
                "Did you get the cake?"
        );

        assertEquals(
                "Message successfully sent.",
                message.SentMessage(1)
        );

        assertTrue(
                Message.getSentMessages().contains(message)
        );

        assertTrue(
                Message.getMessageIDs()
                        .contains(message.getMessageID())
        );

        assertTrue(
                Message.getMessageHashes()
                        .contains(message.getMessageHash())
        );
    }

    // TEST 2: Verify that disregarded messages are recorded
    @Test
    public void testDisregardedMessageCollection() {

        Message message = new Message(
                "+27834484567",
                "Yohoooo, I am at your gate."
        );

        assertEquals(
                "Press 0 to delete the message.",
                message.SentMessage(2)
        );

        assertEquals(
                "Message disregarded.",
                message.disregardMessage(0)
        );

        assertTrue(
                Message.getDisregardedMessages()
                        .contains(message)
        );
    }

    // TEST 3: Verify JSON storage and loading
    @Test
    public void testStoredMessageAndJsonLoading()
            throws IOException {

        Message message = new Message(
                "+27838884567",
                "Where are you? You are late!"
        );

        assertEquals(
                "Message successfully stored.",
                message.SentMessage(3)
        );

        assertTrue(Files.exists(JSON_FILE));

        Message.loadStoredMessages();

        assertNotNull(
                Message.searchByMessageID(
                        message.getMessageID()
                )
        );

        assertEquals(
                1,
                Message.getStoredMessages().size()
        );
    }

    // TEST 4: Search for a message using its ID
    @Test
    public void testSearchByMessageID() {

        Message message = new Message(
                "+27834557896",
                "Hello there"
        );

        message.SentMessage(1);

        Message found =
                Message.searchByMessageID(
                        message.getMessageID()
                );

        assertNotNull(found);

        assertEquals(
                message.getRecipient(),
                found.getRecipient()
        );

        assertEquals(
                message.getMessageText(),
                found.getMessageText()
        );
    }

    // TEST 5: Search for messages using a recipient
    @Test
    public void testSearchByRecipient()
            throws IOException {

        Message first = new Message(
                "+27838884567",
                "First message"
        );

        Message second = new Message(
                "+27838884567",
                "Second message"
        );

        first.SentMessage(3);
        second.SentMessage(3);

        List<Message> matches =
                Message.searchByRecipient(
                        "+27838884567"
                );

        assertEquals(2, matches.size());
    }

    // TEST 6: Identify the longest stored message
    @Test
    public void testLongestStoredMessage() {

        Message shortMessage = new Message(
                "+27838884567",
                "Hello"
        );

        Message longMessage = new Message(
                "+27838884567",
                "Where are you? You are late! "
                + "I have asked you to be on time."
        );

        shortMessage.SentMessage(3);
        longMessage.SentMessage(3);

        Message longest =
                Message.findLongestStoredMessage();

        assertNotNull(longest);

        assertEquals(
                longMessage.getMessageText(),
                longest.getMessageText()
        );
    }

    // TEST 7: Delete a stored message using its hash
    @Test
    public void testDeleteStoredMessageByHash()
            throws IOException {

        Message message = new Message(
                "+27838884567",
                "Ok, I am leaving without you."
        );

        message.SentMessage(3);

        assertTrue(
                Message.deleteMessageByHash(
                        message.getMessageHash()
                )
        );

        assertNull(
                Message.searchByMessageID(
                        message.getMessageID()
                )
        );

        assertFalse(
                Files.readString(JSON_FILE)
                        .contains(message.getMessageID())
        );
    }

    // TEST 8: Verify the full message report
    @Test
    public void testFullMessageReport() {

        Message message = new Message(
                "+27834557896",
                "Did you get the cake?"
        );

        message.SentMessage(1);

        String report =
                Message.displayFullReport();

        assertTrue(
                report.contains(
                        "QUICKCHAT MESSAGE REPORT"
                )
        );

        assertTrue(
                report.contains(
                        "Did you get the cake?"
                )
        );

        assertTrue(
                report.contains(
                        "+27834557896"
                )
        );
    }

    // TEST 9: Search for a message that does not exist
    @Test
    public void testMessageNotFound() {

        assertNull(
                Message.searchByMessageID(
                        "0000000000"
                )
        );
    }

    // TEST 10: Attempt to delete an unknown message hash
    @Test
    public void testDeleteUnknownHash()
            throws IOException {

        assertFalse(
                Message.deleteMessageByHash(
                        "UNKNOWN:HASH"
                )
        );
    }

    // TEST 11: Verify the message ID contains 10 digits
    @Test
    public void testMessageIDHasExactlyTenDigits() {

        Message message = new Message(
                "+27834557896",
                "Hello there"
        );

        assertTrue(message.checkMessageID());

        assertTrue(
                message.getMessageID()
                        .matches("\\d{10}")
        );
    }

    // TEST 12: Verify the correct message hash format
    @Test
    public void testMessageHashFormat() {

        Message message = new Message(
                "+27834557896",
                "Hello there"
        );

        String hash = message.getMessageHash();

        assertTrue(
                hash.matches(
                        "\\d{2}:\\d+:HELLOTHERE"
                )
        );

        assertEquals(
                hash.toUpperCase(),
                hash
        );

        assertTrue(
                hash.startsWith(
                        message.getMessageID()
                                .substring(0, 2) + ":"
                )
        );
    }

    // TEST 13: Verify invalid cellphone numbers are rejected
    @Test
    public void testInvalidRecipientIsRejected() {

        Message message = new Message(
                "12345",
                "This should not send"
        );

        assertEquals(
                "Cell phone number is incorrectly formatted or "
                + "does not contain an international code. "
                + "Please correct the number and try again.",
                message.checkRecipientCell()
        );

        assertNotEquals(
                "Message successfully sent.",
                message.SentMessage(1)
        );

        assertEquals(
                "Pending",
                message.getStatus()
        );
    }

    // TEST 14: Verify multiple messages survive JSON reload
    @Test
    public void testMultipleMessagesPersistAfterReload()
            throws IOException {

        Message first = new Message(
                "+27838884567",
                "Where are you? You are late! "
                + "I have asked you to be on time."
        );

        Message second = new Message(
                "+27838884567",
                "Ok, I am leaving without you."
        );

        assertEquals(
                "Message successfully stored.",
                first.SentMessage(3)
        );

        assertEquals(
                "Message successfully stored.",
                second.SentMessage(3)
        );

        String firstID = first.getMessageID();
        String firstHash = first.getMessageHash();

        String secondID = second.getMessageID();
        String secondHash = second.getMessageHash();

        // Reload stored messages from the JSON file
        Message.loadStoredMessages();

        List<Message> stored =
                Message.getStoredMessages();

        assertEquals(2, stored.size());

        Message loadedFirst =
                Message.searchByMessageID(firstID);

        Message loadedSecond =
                Message.searchByMessageID(secondID);

        assertNotNull(loadedFirst);
        assertNotNull(loadedSecond);

        // Verify original message hashes
        assertEquals(
                firstHash,
                loadedFirst.getMessageHash()
        );

        assertEquals(
                secondHash,
                loadedSecond.getMessageHash()
        );

        // Verify original recipients
        assertEquals(
                first.getRecipient(),
                loadedFirst.getRecipient()
        );

        assertEquals(
                second.getRecipient(),
                loadedSecond.getRecipient()
        );

        // Verify original message text
        assertEquals(
                first.getMessageText(),
                loadedFirst.getMessageText()
        );

        assertEquals(
                second.getMessageText(),
                loadedSecond.getMessageText()
        );

        // Verify both messages have stored status
        assertEquals(
                "Stored",
                loadedFirst.getStatus()
        );

        assertEquals(
                "Stored",
                loadedSecond.getStatus()
        );

        // Verify recipient search finds both messages
        List<Message> matches =
                Message.searchByRecipient(
                        "+27838884567"
                );

        assertEquals(2, matches.size());
    }
}

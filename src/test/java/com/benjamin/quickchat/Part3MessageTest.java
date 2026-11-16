
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

    @BeforeEach
    public void setUp() throws IOException {
        fileExisted = Files.exists(JSON_FILE);

        if (fileExisted) {
            originalFile = Files.readAllBytes(JSON_FILE);
        }

        Files.writeString(JSON_FILE, "[]",
                StandardCharsets.UTF_8);

        Message.loadStoredMessages();
    }

    @AfterEach
    public void tearDown() throws IOException {
        if (fileExisted) {
            Files.write(JSON_FILE, originalFile);
        } else {
            Files.deleteIfExists(JSON_FILE);
        }

        Message.loadStoredMessages();
    }

    @Test
    public void testSentMessageCollection() {
        Message message = new Message(
                "+27834557896", "Did you get the cake?");

        assertEquals("Message successfully sent.",
                message.SentMessage(1));

        assertTrue(Message.getSentMessages()
                .contains(message));

        assertTrue(Message.getMessageIDs()
                .contains(message.getMessageID()));

        assertTrue(Message.getMessageHashes()
                .contains(message.getMessageHash()));
    }

    @Test
    public void testDisregardedMessageCollection() {
        Message message = new Message(
                "+27834484567",
                "Yohoooo, I am at your gate.");

        assertEquals("Press 0 to delete the message.",
                message.SentMessage(2));

        assertEquals("Message disregarded.",
                message.disregardMessage(0));

        assertTrue(Message.getDisregardedMessages()
                .contains(message));
    }

    @Test
    public void testStoredMessageAndJsonLoading()
            throws IOException {

        Message message = new Message(
                "+27838884567",
                "Where are you? You are late!");

        assertEquals("Message successfully stored.",
                message.SentMessage(3));

        assertTrue(Files.exists(JSON_FILE));

        Message.loadStoredMessages();

        assertNotNull(Message.searchByMessageID(
                message.getMessageID()));

        assertEquals(1,
                Message.getStoredMessages().size());
    }

    @Test
    public void testSearchByMessageID() {
        Message message = new Message(
                "+27834557896", "Hello there");

        message.SentMessage(1);

        Message found = Message.searchByMessageID(
                message.getMessageID());

        assertNotNull(found);
        assertEquals(message.getRecipient(),
                found.getRecipient());
        assertEquals(message.getMessageText(),
                found.getMessageText());
    }

    @Test
    public void testSearchByRecipient()
            throws IOException {

        Message first = new Message(
                "+27838884567", "First message");

        Message second = new Message(
                "+27838884567", "Second message");

        first.SentMessage(3);
        second.SentMessage(3);

        List<Message> matches =
                Message.searchByRecipient("+27838884567");

        assertEquals(2, matches.size());
    }

    @Test
    public void testLongestStoredMessage() {
        Message shortMessage = new Message(
                "+27838884567", "Hello");

        Message longMessage = new Message(
                "+27838884567",
                "Where are you? You are late! "
                + "I have asked you to be on time.");

        shortMessage.SentMessage(3);
        longMessage.SentMessage(3);

        Message longest =
                Message.findLongestStoredMessage();

        assertNotNull(longest);
        assertEquals(longMessage.getMessageText(),
                longest.getMessageText());
    }

    @Test
    public void testDeleteStoredMessageByHash()
            throws IOException {

        Message message = new Message(
                "+27838884567",
                "Ok, I am leaving without you.");

        message.SentMessage(3);

        assertTrue(Message.deleteMessageByHash(
                message.getMessageHash()));

        assertNull(Message.searchByMessageID(
                message.getMessageID()));

        assertFalse(Files.readString(JSON_FILE)
                .contains(message.getMessageID()));
    }

    @Test
    public void testFullMessageReport() {
        Message message = new Message(
                "+27834557896",
                "Did you get the cake?");

        message.SentMessage(1);

        String report = Message.displayFullReport();

        assertTrue(report.contains(
                "QUICKCHAT MESSAGE REPORT"));

        assertTrue(report.contains(
                "Did you get the cake?"));

        assertTrue(report.contains(
                "+27834557896"));
    }

    @Test
    public void testMessageNotFound() {
        assertNull(Message.searchByMessageID(
                "0000000000"));
    }

    @Test
    public void testDeleteUnknownHash()
            throws IOException {

        assertFalse(Message.deleteMessageByHash(
                "UNKNOWN:HASH"));
    }
}

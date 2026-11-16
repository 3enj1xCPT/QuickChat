
package com.benjamin.quickchat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Message {

    private static final int MAX_LENGTH = 250;
    private static final SecureRandom RANDOM = new SecureRandom();

    // Part 3 message collections
    private static final List<Message> sentMessages = new ArrayList<>();
    private static final List<Message> disregardedMessages = new ArrayList<>();
    private static final List<Message> storedMessages = new ArrayList<>();

    private static final List<String> messageHashes = new ArrayList<>();
    private static final List<String> messageIDs = new ArrayList<>();

    private static int totalSent = 0;
    private static int nextMessageNumber = 0;

    private static final Path JSON_FILE =
            Paths.get("stored_messages.json");

    private final String messageID;
    private final int messageNumber;
    private final String recipient;
    private final String messageText;
    private final String messageHash;

    private String status;

    // Create a new message
    public Message(String recipient, String messageText) {
        this.messageID = generateMessageID();
        this.messageNumber = nextMessageNumber++;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
        this.status = "Pending";
    }

    // Reconstruct a message that was saved in JSON
    private Message(String id, int number, String recipient,
                    String text, String hash, String status) {

        this.messageID = id;
        this.messageNumber = number;
        this.recipient = recipient;
        this.messageText = text;
        this.messageHash = hash;
        this.status = status;

        nextMessageNumber = Math.max(nextMessageNumber, number + 1);
    }

    // Generate a 10-digit message ID
    private static String generateMessageID() {
        String id;

        do {
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < 10; i++) {
                builder.append(RANDOM.nextInt(10));
            }

            id = builder.toString();

        } while (messageIDs.contains(id));

        return id;
    }

    public boolean checkMessageID() {
        return messageID.matches("\\d{10}");
    }

    // Validate an international cellphone number
    public String checkRecipientCell() {

        if (recipient != null
                && recipient.matches("\\+[1-9]\\d{7,14}")) {

            return "Cell phone number successfully captured.";
        }

        return "Cell phone number is incorrectly formatted or "
                + "does not contain an international code. "
                + "Please correct the number and try again.";
    }

    // Validate message length
    public String checkMessageLength() {

        if (messageText == null
                || messageText.trim().isEmpty()) {

            return "Please enter a message.";
        }

        if (messageText.length() <= MAX_LENGTH) {
            return "Message ready to send.";
        }

        int excess = messageText.length() - MAX_LENGTH;

        return "Message exceeds 250 characters by "
                + excess
                + "; please reduce the size.";
    }

    // Create the required message hash
    public String createMessageHash() {

        if (messageText == null
                || messageText.trim().isEmpty()) {
            return "";
        }

        String[] words = messageText.trim().split("\\s+");

        String firstWord = words[0]
                .replaceAll("[^a-zA-Z0-9]", "");

        String lastWord = words[words.length - 1]
                .replaceAll("[^a-zA-Z0-9]", "");

        String firstTwoDigits = messageID.substring(0, 2);

        return (firstTwoDigits + ":"
                + messageNumber + ":"
                + firstWord + lastWord).toUpperCase();
    }

    // Record the ID and hash of a processed message
    private void recordMessageDetails() {

        if (!messageIDs.contains(messageID)) {
            messageIDs.add(messageID);
        }

        if (!messageHashes.contains(messageHash)) {
            messageHashes.add(messageHash);
        }
    }

    // 1 = Send, 2 = Disregard, 3 = Store
    public String SentMessage(int option) {

        if (option < 1 || option > 3) {
            return "Invalid option.";
        }

        if (!status.equals("Pending")) {
            return "Message has already been processed.";
        }

        if (!checkMessageID()) {
            return "Invalid message ID.";
        }

        if (!checkRecipientCell().equals(
                "Cell phone number successfully captured.")) {
            return checkRecipientCell();
        }

        if (!checkMessageLength().equals(
                "Message ready to send.")) {
            return checkMessageLength();
        }

        switch (option) {

            case 1:
                status = "Sent";
                sentMessages.add(this);
                totalSent++;
                recordMessageDetails();

                return "Message successfully sent.";

            case 2:
                return "Press 0 to delete the message.";

            case 3:
                try {
                    storeMessage();
                    status = "Stored";

                    if (!storedMessages.contains(this)) {
                        storedMessages.add(this);
                    }

                    recordMessageDetails();

                    return "Message successfully stored.";

                } catch (IOException e) {
                    return "Unable to store message: "
                            + e.getMessage();
                }

            default:
                return "Invalid option.";
        }
    }

    // Confirm that a message should be disregarded
    public String disregardMessage(int confirmation) {

        if (!status.equals("Pending")) {
            return "Message has already been processed.";
        }

        if (confirmation == 0) {

            status = "Disregarded";
            disregardedMessages.add(this);
            recordMessageDetails();

            return "Message disregarded.";
        }

        return "Message was not disregarded.";
    }

    // Display recently sent messages
    public static String printMessages() {

        if (sentMessages.isEmpty()) {
            return "No messages have been sent.";
        }

        StringBuilder result = new StringBuilder();

        for (Message current : sentMessages) {
            result.append(formatMessage(current));
        }

        return result.toString();
    }

    public static int returnTotalMessagess() {
        return totalSent;
    }

    // Part 3: Access message collections safely
    public static List<Message> getSentMessages() {
        return Collections.unmodifiableList(
                new ArrayList<>(sentMessages));
    }

    public static List<Message> getDisregardedMessages() {
        return Collections.unmodifiableList(
                new ArrayList<>(disregardedMessages));
    }

    public static List<Message> getStoredMessages() {
        return Collections.unmodifiableList(
                new ArrayList<>(storedMessages));
    }

    public static List<String> getMessageHashes() {
        return Collections.unmodifiableList(
                new ArrayList<>(messageHashes));
    }

    public static List<String> getMessageIDs() {
        return Collections.unmodifiableList(
                new ArrayList<>(messageIDs));
    }

    // Display sender and recipient of sent messages
    public static String displaySendersAndRecipients(String sender) {

        if (sentMessages.isEmpty()) {
            return "No sent messages available.";
        }

        StringBuilder result = new StringBuilder();

        for (Message message : sentMessages) {

            result.append("Sender: ")
                    .append(sender)
                    .append("\nRecipient: ")
                    .append(message.recipient)
                    .append("\n-------------------------\n");
        }

        return result.toString();
    }

    // Find the longest sent message
    public static Message findLongestSentMessage() {

        Message longest = null;

        for (Message message : sentMessages) {

            if (longest == null
                    || message.messageText.length()
                    > longest.messageText.length()) {

                longest = message;
            }
        }

        return longest;
    }

    // Find the longest stored message
    public static Message findLongestStoredMessage() {

        Message longest = null;

        for (Message message : storedMessages) {

            if (longest == null
                    || message.messageText.length()
                    > longest.messageText.length()) {

                longest = message;
            }
        }

        return longest;
    }

    // Search all active messages by message ID
    public static Message searchByMessageID(String id) {

        for (Message message : getActiveMessages()) {

            if (message.messageID.equals(id)) {
                return message;
            }
        }

        return null;
    }

    // Search sent and stored messages by recipient
    public static List<Message> searchByRecipient(String recipient) {

        List<Message> matches = new ArrayList<>();

        for (Message message : getActiveMessages()) {

            if (message.recipient.equals(recipient)) {
                matches.add(message);
            }
        }

        return matches;
    }

    // Delete a message using its hash
    public static boolean deleteMessageByHash(String hash)
            throws IOException {

        if (hash == null) {
            return false;
        }

        for (Message message : new ArrayList<>(storedMessages)) {

            if (message.messageHash.equalsIgnoreCase(hash)) {

                List<Message> remaining =
                        new ArrayList<>(storedMessages);

                remaining.remove(message);

                // Write the updated JSON before changing memory
                saveStoredMessages(remaining);

                storedMessages.remove(message);
                messageHashes.remove(message.messageHash);
                messageIDs.remove(message.messageID);

                message.status = "Deleted";

                return true;
            }
        }

        return false;
    }

    // Generate a complete report
    public static String displayFullReport() {

        StringBuilder report = new StringBuilder();

        report.append("\n=== QUICKCHAT MESSAGE REPORT ===\n");

        report.append("\nTotal sent messages: ")
                .append(sentMessages.size());

        report.append("\nTotal stored messages: ")
                .append(storedMessages.size());

        report.append("\nTotal disregarded messages: ")
                .append(disregardedMessages.size());

        report.append("\n\n=== SENT MESSAGES ===\n");

        if (sentMessages.isEmpty()) {
            report.append("No sent messages.\n");
        }

        for (Message message : sentMessages) {
            report.append(formatMessage(message));
        }

        report.append("\n=== STORED MESSAGES ===\n");

        if (storedMessages.isEmpty()) {
            report.append("No stored messages.\n");
        }

        for (Message message : storedMessages) {
            report.append(formatMessage(message));
        }

        report.append("\n=== DISREGARDED MESSAGES ===\n");

        if (disregardedMessages.isEmpty()) {
            report.append("No disregarded messages.\n");
        }

        for (Message message : disregardedMessages) {
            report.append(formatMessage(message));
        }

        return report.toString();
    }

    // Format one message for reports and searches
    public static String formatMessage(Message message) {

        if (message == null) {
            return "Message not found.";
        }

        return "\nMessage ID: " + message.messageID
                + "\nMessage Hash: " + message.messageHash
                + "\nRecipient: " + message.recipient
                + "\nMessage: " + message.messageText
                + "\nStatus: " + message.status
                + "\n-------------------------\n";
    }

    // Combine sent and stored messages for searching
    private static List<Message> getActiveMessages() {

        List<Message> messages = new ArrayList<>();
        messages.addAll(sentMessages);
        messages.addAll(storedMessages);

        return messages;
    }

    // Escape characters so that JSON remains valid
    private static String escapeJson(String value) {

        if (value == null) {
            return "";
        }

        StringBuilder escaped = new StringBuilder();

        for (char character : value.toCharArray()) {

            switch (character) {

                case '"':
                    escaped.append("\\\"");
                    break;

                case '\\':
                    escaped.append("\\\\");
                    break;

                case '\n':
                    escaped.append("\\n");
                    break;

                case '\r':
                    escaped.append("\\r");
                    break;

                case '\t':
                    escaped.append("\\t");
                    break;

                default:
                    if (character < 0x20) {
                        escaped.append(String.format(
                                "\\u%04x", (int) character));
                    } else {
                        escaped.append(character);
                    }
            }
        }

        return escaped.toString();
    }

    // Convert one message to JSON
    private String toJson() {

        return "{"
                + "\"messageID\":\""
                + escapeJson(messageID) + "\","

                + "\"messageNumber\":"
                + messageNumber + ","

                + "\"recipient\":\""
                + escapeJson(recipient) + "\","

                + "\"message\":\""
                + escapeJson(messageText) + "\","

                + "\"messageHash\":\""
                + escapeJson(messageHash) + "\","

                + "\"status\":\"Stored\""
                + "}";
    }

    // Save one message without overwriting previous messages
    public void storeMessage() throws IOException {

        List<Message> existing = readMessagesFromJson();

        for (Message message : existing) {
            if (message.messageID.equals(this.messageID)) {
                return;
            }
        }

        existing.add(this);
        saveStoredMessages(existing);
    }

    // Rewrite JSON after storing or deleting messages
    private static void saveStoredMessages(List<Message> messages)
            throws IOException {

        StringBuilder json = new StringBuilder("[\n");

        for (int i = 0; i < messages.size(); i++) {

            json.append("  ")
                    .append(messages.get(i).toJson());

            if (i < messages.size() - 1) {
                json.append(",");
            }

            json.append("\n");
        }

        json.append("]");

        Files.writeString(
                JSON_FILE,
                json.toString(),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );
    }

    // Read messages from JSON and recreate Message objects
    private static List<Message> readMessagesFromJson()
            throws IOException {

        List<Message> messages = new ArrayList<>();

        if (!Files.exists(JSON_FILE)) {
            return messages;
        }

        String json = Files.readString(
                JSON_FILE,
                StandardCharsets.UTF_8
        ).trim();

        if (json.isEmpty() || json.equals("[]")) {
            return messages;
        }

        // Parse JSON objects without adding a Maven dependency
        JsonReader reader = new JsonReader(json);
        List<Map<String, String>> records = reader.readArray();

        for (Map<String, String> record : records) {

            try {
                String id = required(record, "messageID");
                int number = Integer.parseInt(
                        required(record, "messageNumber"));

                String recipient = required(record, "recipient");
                String text = required(record, "message");
                String hash = required(record, "messageHash");

                Message message = new Message(
                        id,
                        number,
                        recipient,
                        text,
                        hash,
                        "Stored"
                );

                messages.add(message);

            } catch (NumberFormatException e) {
                throw new IOException(
                        "Invalid message number in JSON.", e);
            }
        }

        return messages;
    }

    private static String required(
            Map<String, String> record, String key)
            throws IOException {

        String value = record.get(key);

        if (value == null) {
            throw new IOException(
                    "Missing JSON field: " + key);
        }

        return value;
    }

    // Load stored messages when the application starts
    public static void loadStoredMessages() throws IOException {

        List<Message> loaded = readMessagesFromJson();

        storedMessages.clear();

        for (Message message : loaded) {
            storedMessages.add(message);
        }

        rebuildMessageDetails();
    }

    // Rebuild ID and hash collections from processed messages
    private static void rebuildMessageDetails() {

        messageIDs.clear();
        messageHashes.clear();

        List<Message> all = new ArrayList<>();

        all.addAll(sentMessages);
        all.addAll(storedMessages);
        all.addAll(disregardedMessages);

        for (Message message : all) {

            if (!messageIDs.contains(message.messageID)) {
                messageIDs.add(message.messageID);
            }

            if (!messageHashes.contains(message.messageHash)) {
                messageHashes.add(message.messageHash);
            }
        }
    }

    // Small JSON reader for the structure saved by QuickChat
    private static class JsonReader {

        private final String text;
        private int position = 0;

        JsonReader(String text) {
            this.text = text;
        }

        List<Map<String, String>> readArray() throws IOException {

            List<Map<String, String>> result = new ArrayList<>();

            expect('[');
            skipSpaces();

            if (consume(']')) {
                ensureFinished();
                return result;
            }

            while (true) {

                result.add(readObject());

                if (consume(']')) {
                    break;
                }

                expect(',');
            }

            ensureFinished();
            return result;
        }

        private Map<String, String> readObject()
                throws IOException {

            Map<String, String> object = new HashMap<>();

            expect('{');

            if (consume('}')) {
                return object;
            }

            while (true) {

                String key = readString();
                expect(':');

                String value;

                skipSpaces();

                if (position < text.length()
                        && text.charAt(position) == '"') {

                    value = readString();

                } else {

                    int start = position;

                    while (position < text.length()) {

                        char c = text.charAt(position);

                        if (c == ',' || c == '}'
                                || Character.isWhitespace(c)) {
                            break;
                        }

                        position++;
                    }

                    value = text.substring(start, position);

                    if (value.isEmpty()) {
                        throw new IOException("Invalid JSON value.");
                    }
                }

                object.put(key, value);

                if (consume('}')) {
                    return object;
                }

                expect(',');
            }
        }

        private String readString() throws IOException {

            skipSpaces();
            expect('"');

            StringBuilder result = new StringBuilder();

            while (position < text.length()) {

                char c = text.charAt(position++);

                if (c == '"') {
                    return result.toString();
                }

                if (c == '\\') {

                    if (position >= text.length()) {
                        throw new IOException("Invalid JSON escape.");
                    }

                    char escaped = text.charAt(position++);

                    switch (escaped) {

                        case '"':
                            result.append('"');
                            break;

                        case '\\':
                            result.append('\\');
                            break;

                        case '/':
                            result.append('/');
                            break;

                        case 'n':
                            result.append('\n');
                            break;

                        case 'r':
                            result.append('\r');
                            break;

                        case 't':
                            result.append('\t');
                            break;

                        case 'b':
                            result.append('\b');
                            break;

                        case 'f':
                            result.append('\f');
                            break;

                        case 'u':

                            if (position + 4 > text.length()) {
                                throw new IOException(
                                        "Invalid Unicode escape.");
                            }

                            String hex = text.substring(
                                    position, position + 4);

                            try {
                                result.append((char)
                                        Integer.parseInt(hex, 16));
                            } catch (NumberFormatException e) {
                                throw new IOException(
                                        "Invalid Unicode escape.", e);
                            }

                            position += 4;
                            break;

                        default:
                            throw new IOException(
                                    "Invalid JSON escape.");
                    }

                } else {

                    if (c < 0x20) {
                        throw new IOException(
                                "Invalid control character in JSON.");
                    }

                    result.append(c);
                }
            }

            throw new IOException("Unclosed JSON string.");
        }

        private void skipSpaces() {

            while (position < text.length()
                    && Character.isWhitespace(
                            text.charAt(position))) {

                position++;
            }
        }

        private boolean consume(char expected) {

            skipSpaces();

            if (position < text.length()
                    && text.charAt(position) == expected) {

                position++;
                return true;
            }

            return false;
        }

        private void expect(char expected) throws IOException {

            if (!consume(expected)) {
                throw new IOException(
                        "Invalid JSON at position " + position
                        + ". Expected " + expected);
            }
        }

        private void ensureFinished() throws IOException {

            skipSpaces();

            if (position != text.length()) {
                throw new IOException(
                        "Unexpected data after JSON array.");
            }
        }
    }

    // Existing getters

    public String getMessageID() {
        return messageID;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getStatus() {
        return status;
    }
}

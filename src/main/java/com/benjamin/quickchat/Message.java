
package com.benjamin.quickchat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class Message {

    private static final int MAX_LENGTH = 250;
    private static final SecureRandom RANDOM = new SecureRandom();

    private static final List<Message> sentMessages = new ArrayList<>();
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

    // Generate a random 10-digit message ID
    private static String generateMessageID() {
        StringBuilder id = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            id.append(RANDOM.nextInt(10));
        }

        return id.toString();
    }

    // Check that the ID contains exactly 10 digits
    public boolean checkMessageID() {
        return messageID.matches("\\d{10}");
    }

    // Validate a South African international cellphone number
    public String checkRecipientCell() {
        if (recipient != null
                && recipient.matches("\\+27[6-8]\\d{8}")) {

            return "Cell phone number successfully captured.";
        }

        return "Cell phone number is incorrectly formatted or "
                + "does not contain an international code. "
                + "Please correct the number and try again.";
    }

    // Validate the message length
    public String checkMessageLength() {
if (messageText == null || messageText.trim().isEmpty()) {
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

    // Generate the message hash
    public String createMessageHash() {

        if (messageText == null
                || messageText.trim().isEmpty()) {
            return "";
        }

        String[] words = messageText.trim().split("\\s+");

String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "");
String lastWord = words[words.length - 1]
        .replaceAll("[^a-zA-Z0-9]", "");

        String firstTwoDigits = messageID.substring(0, 2);

        return (firstTwoDigits + ":"
                + messageNumber + ":"
                + firstWord + lastWord).toUpperCase();
    }

    // Process a message action
    // 1 = Send, 2 = Disregard, 3 = Store
    public String SentMessage(int option) {

        if (option < 1 || option > 3) {
            return "Invalid option.";
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

        if (!status.equals("Pending")) {
            return "Message has already been processed.";
        }

        switch (option) {

            case 1:
                status = "Sent";
                sentMessages.add(this);
                totalSent++;
                return "Message successfully sent.";

            case 2:
                return "Press 0 to delete the message.";

            case 3:
                try {
                    storeMessage();
                    status = "Stored";
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
            return "Message disregarded.";
        }

        return "Message was not disregarded.";
    }

    // Display all sent messages
    public static String printMessages() {

        if (sentMessages.isEmpty()) {
            return "No messages have been sent.";
        }

        StringBuilder result = new StringBuilder();

        for (Message current : sentMessages) {

            result.append("\nMessage ID: ")
                    .append(current.messageID)
                    .append("\nMessage Hash: ")
                    .append(current.messageHash)
                    .append("\nRecipient: ")
                    .append(current.recipient)
                    .append("\nMessage: ")
                    .append(current.messageText)
                    .append("\n-------------------------\n");
        }

        return result.toString();
    }

    // Return the number of successfully sent messages
    public static int returnTotalMessagess() {
        return totalSent;
    }

    // Escape special characters for valid JSON
    private static String escapeJson(String value) {

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
                        escaped.append(
                                String.format("\\u%04x", (int) character)
                        );
                    } else {
                        escaped.append(character);
                    }
            }
        }

        return escaped.toString();
    }

    // Convert the message to JSON
    private String toJson() {

        return "{"
                + "\"messageID\":\"" + escapeJson(messageID) + "\","
                + "\"messageNumber\":" + messageNumber + ","
                + "\"recipient\":\"" + escapeJson(recipient) + "\","
                + "\"message\":\"" + escapeJson(messageText) + "\","
                + "\"messageHash\":\"" + escapeJson(messageHash) + "\","
                + "\"status\":\"Stored\""
                + "}";
    }

    // Save stored messages in a JSON file
    public void storeMessage() throws IOException {

        String existing = "";

        if (Files.exists(JSON_FILE)) {
            existing = Files.readString(
                    JSON_FILE, StandardCharsets.UTF_8
            ).trim();
        }

        String newMessage = toJson();
        String updated;

        if (existing.isEmpty() || existing.equals("[]")) {

            updated = "[\n  " + newMessage + "\n]";

        } else if (existing.startsWith("[")
                && existing.endsWith("]")) {

            String content = existing.substring(
                    1, existing.length() - 1
            ).trim();

            if (content.isEmpty()) {
                updated = "[\n  " + newMessage + "\n]";
            } else {
                updated = "[\n  " + content
                        + ",\n  " + newMessage + "\n]";
            }

        } else {
            throw new IOException(
                    "Existing JSON file has an invalid structure."
            );
        }

        Files.writeString(
                JSON_FILE,
                updated,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );
    }

    // Getters for testing and application integration
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

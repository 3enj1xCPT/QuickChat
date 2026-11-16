
package com.benjamin.quickchat;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class QuickChat {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("=== QuickChat Registration ===");

        System.out.print("Enter your first name: ");
        String firstName = readLine();

        System.out.print("Enter your last name: ");
        String lastName = readLine();

        System.out.print("Enter a username: ");
        String username = readLine();

        System.out.print("Enter a password: ");
        String password = readLine();

        System.out.print("Enter your cellphone number: ");
        String cellPhoneNumber = readLine();

        // Create user using the existing Login class
        Login user = new Login(
                username,
                password,
                cellPhoneNumber,
                firstName,
                lastName
        );

        // Display registration validation results
        if (user.checkUserName()) {
            System.out.println(
                    "Username successfully captured.");
        } else {
            System.out.println(
                    "Username is not correctly formatted; "
                    + "please ensure that your username "
                    + "contains an underscore and is no more "
                    + "than five characters in length.");
        }

        if (user.checkPasswordComplexity()) {
            System.out.println(
                    "Password successfully captured.");
        } else {
            System.out.println(
                    "Password is not correctly formatted; "
                    + "please ensure that the password "
                    + "contains at least eight characters, "
                    + "a capital letter, a number, "
                    + "and a special character.");
        }

        if (user.checkCellPhoneNumber()) {
            System.out.println(
                    "Cell phone number successfully added.");
        } else {
            System.out.println(
                    "Cell phone number incorrectly formatted "
                    + "or does not contain international code.");
        }

        String registrationResult = user.registerUser();

        if (!registrationResult.equals(
                "User successfully registered.")) {

            System.out.println(registrationResult);
            System.out.println(
                    "Registration unsuccessful. "
                    + "QuickChat cannot continue.");
            return;
        }

        System.out.println("User successfully registered.");

        System.out.println();
        System.out.println("=== QuickChat Login ===");

        System.out.print("Enter your username: ");
        String loginUsername = readLine();

        System.out.print("Enter your password: ");
        String loginPassword = readLine();

        System.out.println(
                user.returnLoginStatus(
                        loginUsername, loginPassword));

        if (!user.loginUser(
                loginUsername, loginPassword)) {

            System.out.println(
                    "Access denied. Please log in "
                    + "successfully to use QuickChat.");
            return;
        }

        System.out.println();
        System.out.println("Welcome to QuickChat.");

        // Load stored messages from JSON at startup
        try {
            Message.loadStoredMessages();

            System.out.println(
                    "Stored messages loaded successfully.");

        } catch (IOException e) {

            System.out.println(
                    "Warning: Unable to load stored messages: "
                    + e.getMessage());

            System.out.println(
                    "Existing stored messages may not "
                    + "be available until the JSON file "
                    + "is corrected.");
        }

        // Ask how many messages may be processed
        int messageLimit = readPositiveNumber(
                "How many messages would you like to enter? ");

        int messagesEntered = 0;
        boolean running = true;

        while (running) {

            System.out.println();
            System.out.println(
                    "=== QuickChat Main Menu ===");

            System.out.println("1) Send Messages");
            System.out.println(
                    "2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");

            System.out.println(
                    "Messages remaining: "
                    + (messageLimit - messagesEntered));

            System.out.print("Select an option (1-4): ");
            String choice = readLine().trim();

            switch (choice) {

                case "1":

                    if (messagesEntered >= messageLimit) {

                        System.out.println(
                                "You have reached your message "
                                + "limit of " + messageLimit + ".");

                    } else {

                        boolean processed = composeMessage();

                        if (processed) {

                            messagesEntered++;

                            System.out.println(
                                    "Messages processed: "
                                    + messagesEntered
                                    + " of " + messageLimit);
                        }
                    }

                    break;

                case "2":

                    System.out.println();
                    System.out.println(
                            "=== Recently Sent Messages ===");

                    System.out.println(
                            Message.printMessages());

                    break;

                case "3":

                    System.out.println();
                    System.out.println(
                            "=== QuickChat Summary ===");

                    System.out.println(
                            "Total messages successfully sent: "
                            + Message.returnTotalMessagess());

                    System.out.println(
                            Message.printMessages());

                    System.out.println(
                            "Thank you for using QuickChat.");

                    running = false;
                    break;

                case "4":

                    storedMessagesMenu(cellPhoneNumber);
                    break;

                default:

                    System.out.println(
                            "Invalid option. "
                            + "Please select 1, 2, 3 or 4.");

                    break;
            }
        }
    }

    // Read user input safely
    private static String readLine() {

        if (!scanner.hasNextLine()) {
            System.out.println(
                    "\nInput closed. Exiting QuickChat.");
            System.exit(0);
        }

        return scanner.nextLine();
    }

    // Read a positive whole number
    private static int readPositiveNumber(String prompt) {

        while (true) {

            System.out.print(prompt);

            String input = readLine().trim();

            try {

                int number = Integer.parseInt(input);

                if (number > 0) {
                    return number;
                }

                System.out.println(
                        "Please enter a number greater than zero.");

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid whole number.");
            }
        }
    }

    // Compose and process a message
    private static boolean composeMessage() {

        System.out.println();
        System.out.println("=== Compose Message ===");

        System.out.print(
                "Enter recipient cellphone number: ");

        String recipient = readLine();

        System.out.print("Enter your message: ");
        String messageText = readLine();

        Message message = new Message(
                recipient, messageText);

        // Validate recipient
        String recipientResult =
                message.checkRecipientCell();

        if (!recipientResult.equals(
                "Cell phone number successfully captured.")) {

            System.out.println(recipientResult);
            return false;
        }

        // Validate message length
        String lengthResult =
                message.checkMessageLength();

        if (!lengthResult.equals(
                "Message ready to send.")) {

            System.out.println(lengthResult);
            return false;
        }

        // Validate message ID
        if (!message.checkMessageID()) {

            System.out.println(
                    "The generated message ID is invalid.");

            return false;
        }

        // Display generated message information
        System.out.println();
        System.out.println("=== Message Details ===");

        System.out.println(
                "Message ID: "
                + message.getMessageID());

        System.out.println(
                "Message Hash: "
                + message.getMessageHash());

        System.out.println(
                "Recipient: "
                + message.getRecipient());

        System.out.println(
                "Message: "
                + message.getMessageText());

        // Message processing options
        System.out.println();
        System.out.println("=== Message Options ===");

        System.out.println("1) Send Message");
        System.out.println("2) Disregard Message");
        System.out.println("3) Store Message");

        System.out.print(
                "Choose an option (1-3): ");

        int action;

        try {

            action = Integer.parseInt(
                    readLine().trim());

        } catch (NumberFormatException e) {

            System.out.println(
                    "Invalid message action.");

            return false;
        }

        if (action < 1 || action > 3) {

            System.out.println(
                    "Invalid message action. "
                    + "Please select 1, 2 or 3.");

            return false;
        }

        String result = message.SentMessage(action);

        System.out.println(result);

        // Confirm disregarding a message
        if (action == 2
                && result.equals(
                        "Press 0 to delete the message.")) {

            System.out.print(
                    "Enter 0 to confirm deletion: ");

            try {

                int confirmation = Integer.parseInt(
                        readLine().trim());

                System.out.println(
                        message.disregardMessage(
                                confirmation));

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid confirmation. "
                        + "Message not disregarded.");
            }
        }

        // Count only successfully processed messages
        return message.getStatus().equals("Sent")
                || message.getStatus().equals("Stored")
                || message.getStatus().equals("Disregarded");
    }

    // Part 3 stored messages submenu
    private static void storedMessagesMenu(String sender) {

        boolean inMenu = true;

        while (inMenu) {

            System.out.println();
            System.out.println(
                    "=== STORED MESSAGES MENU ===");

            System.out.println(
                    "1) Display sender and recipient");

            System.out.println(
                    "2) Display longest stored message");

            System.out.println(
                    "3) Search message by ID");

            System.out.println(
                    "4) Search messages by recipient");

            System.out.println(
                    "5) Delete stored message by hash");

            System.out.println(
                    "6) Display full message report");

            System.out.println(
                    "7) View all stored messages");

            System.out.println(
                    "8) Return to main menu");

            System.out.print(
                    "Select an option (1-8): ");

            String choice = readLine().trim();

            switch (choice) {

                case "1":

                    displaySenderAndRecipient(sender);
                    break;

                case "2":

                    displayLongestStoredMessage();
                    break;

                case "3":

                    searchMessageByID();
                    break;

                case "4":

                    searchMessagesByRecipient();
                    break;

                case "5":

                    deleteStoredMessage();
                    break;

                case "6":

                    System.out.println(
                            Message.displayFullReport());

                    break;

                case "7":

                    displayStoredMessages();
                    break;

                case "8":

                    inMenu = false;
                    break;

                default:

                    System.out.println(
                            "Invalid option. "
                            + "Please select 1 to 8.");

                    break;
            }
        }
    }

    // Display sender and recipient details
    private static void displaySenderAndRecipient(
            String sender) {

        System.out.println();
        System.out.println(
                "=== Sender and Recipient Details ===");

        List<Message> stored =
                Message.getStoredMessages();

        if (stored.isEmpty()) {

            System.out.println(
                    "No stored messages available.");

            return;
        }

        for (Message message : stored) {

            System.out.println(
                    "Sender: " + sender);

            System.out.println(
                    "Recipient: "
                    + message.getRecipient());

            System.out.println(
                    "Message ID: "
                    + message.getMessageID());

            System.out.println(
                    "-------------------------");
        }
    }

    // Display longest stored message
    private static void displayLongestStoredMessage() {

        System.out.println();
        System.out.println(
                "=== Longest Stored Message ===");

        Message longest =
                Message.findLongestStoredMessage();

        if (longest == null) {

            System.out.println(
                    "No stored messages available.");

        } else {

            System.out.println(
                    Message.formatMessage(longest));

            System.out.println(
                    "Message length: "
                    + longest.getMessageText().length()
                    + " characters");
        }
    }

    // Search for a message by its 10-digit ID
    private static void searchMessageByID() {

        System.out.println();
        System.out.println(
                "=== Search Message by ID ===");

        System.out.print(
                "Enter the 10-digit message ID: ");

        String id = readLine().trim();

        Message found =
                Message.searchByMessageID(id);

        if (found == null) {

            System.out.println(
                    "Message not found.");

        } else {

            System.out.println(
                    "Message found:");

            System.out.println(
                    Message.formatMessage(found));
        }
    }

    // Search messages using a recipient cellphone number
    private static void searchMessagesByRecipient() {

        System.out.println();
        System.out.println(
                "=== Search Messages by Recipient ===");

        System.out.print(
                "Enter recipient cellphone number: ");

        String recipient = readLine().trim();

        List<Message> matches =
                Message.searchByRecipient(recipient);

        if (matches.isEmpty()) {

            System.out.println(
                    "No messages found for this recipient.");

            return;
        }

        System.out.println(
                "Messages found: " + matches.size());

        for (Message message : matches) {

            System.out.println(
                    Message.formatMessage(message));
        }
    }

    // Delete a stored message by its hash
    private static void deleteStoredMessage() {

        System.out.println();
        System.out.println(
                "=== Delete Stored Message ===");

        System.out.print(
                "Enter the message hash: ");

        String hash = readLine().trim();

        try {

            boolean deleted =
                    Message.deleteMessageByHash(hash);

            if (deleted) {

                System.out.println(
                        "Message successfully deleted.");

            } else {

                System.out.println(
                        "No stored message found "
                        + "with that hash.");
            }

        } catch (IOException e) {

            System.out.println(
                    "Unable to delete message: "
                    + e.getMessage());
        }
    }

    // Display all stored messages
    private static void displayStoredMessages() {

        System.out.println();
        System.out.println(
                "=== All Stored Messages ===");

        List<Message> stored =
                Message.getStoredMessages();

        if (stored.isEmpty()) {

            System.out.println(
                    "No stored messages available.");

            return;
        }

        for (Message message : stored) {

            System.out.println(
                    Message.formatMessage(message));
        }
    }
}

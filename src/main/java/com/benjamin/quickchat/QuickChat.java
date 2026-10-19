
package com.benjamin.quickchat;

import java.util.Scanner;

public class QuickChat {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("=== QuickChat Registration ===");

        // Collect user details
        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter a username: ");
        String username = scanner.nextLine();

        System.out.print("Enter a password: ");
        String password = scanner.nextLine();

        System.out.print("Enter your cellphone number: ");
        String cellPhoneNumber = scanner.nextLine();

        // Create the user using the existing Login class
        Login user = new Login(
                username,
                password,
                cellPhoneNumber,
                firstName,
                lastName
        );

        // Validate username
        if (user.checkUserName()) {
            System.out.println("Username successfully captured.");
        } else {
            System.out.println(
                    "Username is not correctly formatted; please ensure that "
                    + "your username contains an underscore and is no more than "
                    + "five characters in length."
            );
        }

        // Validate password
        if (user.checkPasswordComplexity()) {
            System.out.println("Password successfully captured.");
        } else {
            System.out.println(
                    "Password is not correctly formatted; please ensure that "
                    + "the password contains at least eight characters, a capital "
                    + "letter, a number, and a special character."
            );
        }

        // Validate cellphone number
        if (user.checkCellPhoneNumber()) {
            System.out.println("Cell phone number successfully added.");
        } else {
            System.out.println(
                    "Cell phone number incorrectly formatted or does not contain "
                    + "international code."
            );
        }

        // Register the user
        String registrationResult = user.registerUser();

        if (registrationResult.equals("User successfully registered.")) {

            System.out.println("User successfully registered.");

            System.out.println();
            System.out.println("=== QuickChat Login ===");

            System.out.print("Enter your username: ");
            String loginUsername = scanner.nextLine();

            System.out.print("Enter your password: ");
            String loginPassword = scanner.nextLine();

            // Display the login result
            System.out.println(
                    user.returnLoginStatus(loginUsername, loginPassword)
            );

            // Only authenticated users can access QuickChat
            if (user.loginUser(loginUsername, loginPassword)) {

                System.out.println();
                System.out.println("Welcome to QuickChat.");

                // Ask the user how many messages they want to enter
                int messageLimit = 0;

                while (messageLimit <= 0) {

                    System.out.print(
                            "How many messages would you like to enter? "
                    );

                    try {
                        messageLimit = Integer.parseInt(
                                scanner.nextLine().trim()
                        );

                        if (messageLimit <= 0) {
                            System.out.println(
                                    "Please enter a number greater than zero."
                            );
                        }

                    } catch (NumberFormatException e) {
                        System.out.println(
                                "Please enter a valid whole number."
                        );
                    }
                }

                int messagesEntered = 0;
                boolean running = true;

                // Keep displaying the menu until the user quits
                while (running) {

                    System.out.println();
                    System.out.println("=== QuickChat Main Menu ===");
                    System.out.println("1) Send Messages");
                    System.out.println("2) Show recently sent messages");
                    System.out.println("3) Quit");

                    System.out.println(
                            "Messages remaining: "
                            + (messageLimit - messagesEntered)
                    );

System.out.print("Select an option (1-3): ");

if (!scanner.hasNextLine()) {
    System.out.println("\nInput closed. Exiting QuickChat.");
    break;
}

String choice = scanner.nextLine();

                    switch (choice) {

                        case "1": {

                            // Prevent exceeding the message limit
                            if (messagesEntered >= messageLimit) {

                                System.out.println(
                                        "You have reached your message limit of "
                                        + messageLimit + "."
                                );
                                break;
                            }

                            // Use the requested message limit to control entry.
                            // Each successful send, store or disregard fills one slot.
                            for (int slot = messagesEntered; slot < messageLimit; slot++) {

                            System.out.println();
                            System.out.println("=== Compose Message ===");

                            // Collect recipient
                            System.out.print(
                                    "Enter recipient cellphone number: "
                            );
                            String recipient = scanner.nextLine();

                            // Collect message content
                            System.out.print("Enter your message: ");
                            String messageText = scanner.nextLine();

                            // Create the message object
                            Message message = new Message(
                                    recipient,
                                    messageText
                            );

                            // Validate recipient
                            String recipientResult =
                                    message.checkRecipientCell();

                            if (!recipientResult.equals(
                                    "Cell phone number successfully captured."
                            )) {

                                System.out.println(recipientResult);
                                break;
                            }

                            // Validate message length
                            String lengthResult =
                                    message.checkMessageLength();

                            if (!lengthResult.equals(
                                    "Message ready to send."
                            )) {

                                System.out.println(lengthResult);
                                break;
                            }

                            // Validate message ID
                            if (!message.checkMessageID()) {

                                System.out.println(
                                        "The generated message ID is invalid."
                                );
                                break;
                            }

                            // Display generated message information
                            System.out.println();
                            System.out.println(
                                    "=== Message Details ==="
                            );

                            System.out.println(
                                    "Message ID: "
                                    + message.getMessageID()
                            );

                            System.out.println(
                                    "Message Hash: "
                                    + message.getMessageHash()
                            );

                            System.out.println(
                                    "Recipient: "
                                    + message.getRecipient()
                            );

                            System.out.println(
                                    "Message: "
                                    + message.getMessageText()
                            );

                            // Message processing options
                            System.out.println();
                            System.out.println(
                                    "=== Message Options ==="
                            );

                            System.out.println("1) Send Message");
                            System.out.println("2) Disregard Message");
                            System.out.println("3) Store Message");

                            System.out.print(
                                    "Choose an option (1-3): "
                            );

                            String actionInput = scanner.nextLine();

                            int action;

                            try {
                                action = Integer.parseInt(
                                        actionInput.trim()
                                );

                            } catch (NumberFormatException e) {

                                System.out.println(
                                        "Invalid message action."
                                );
                                break;
                            }

                            if (action < 1 || action > 3) {

                                System.out.println(
                                        "Invalid message action. "
                                        + "Please select 1, 2 or 3."
                                );
                                break;
                            }

                            // Send, disregard or store the message
                            String result = message.SentMessage(action);

                            System.out.println(result);

                            // Confirm deletion when disregarding
                            if (action == 2
                                    && result.equals(
                                            "Press 0 to delete the message."
                                    )) {

                                System.out.print(
                                        "Enter 0 to confirm deletion: "
                                );

                                try {

                                    int confirmation =
                                            Integer.parseInt(
                                                    scanner.nextLine().trim()
                                            );

                                    System.out.println(
                                            message.disregardMessage(
                                                    confirmation
                                            )
                                    );

                                } catch (NumberFormatException e) {

                                    System.out.println(
                                            "Invalid confirmation. "
                                            + "Message not deleted."
                                    );
                                }
                            }

                            // Count successfully processed messages
                            if (message.getStatus().equals("Sent")
                                    || message.getStatus().equals("Stored")
                                    || message.getStatus().equals(
                                            "Disregarded"
                                    )) {

                                messagesEntered++;

                                System.out.println(
                                        "Messages processed: "
                                        + messagesEntered
                                        + " of "
                                        + messageLimit
                                );
                            }

                            } // End message-entry for loop
                            break;
                        }

                        case "2":

                            // Required Part 2 placeholder
                            System.out.println("Coming Soon.");
                            break;

                        case "3":

                            // Display the final messaging summary
                            System.out.println();
                            System.out.println(
                                    "=== QuickChat Summary ==="
                            );

                            System.out.println(
                                    "Total messages successfully sent: "
                                    + Message.returnTotalMessagess()
                            );

                            System.out.println(
                                    Message.printMessages()
                            );

                            System.out.println(
                                    "Thank you for using QuickChat."
                            );

                            running = false;
                            break;

                        default:

                            System.out.println(
                                    "Invalid option. "
                                    + "Please select 1, 2 or 3."
                            );
                            break;
                    }
                }

            } else {

                System.out.println(
                        "Access denied. Please log in successfully "
                        + "to use QuickChat."
                );
            }

        } else {

            System.out.println(registrationResult);

            System.out.println(
                    "Registration unsuccessful. "
                    + "QuickChat cannot continue."
            );
        }

        scanner.close();
    }
}

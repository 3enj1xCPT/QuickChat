
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

                boolean running = true;

                // Keep displaying the menu until the user quits
                while (running) {

                    System.out.println();
                    System.out.println("=== QuickChat Main Menu ===");
                    System.out.println("1) Send Messages");
                    System.out.println("2) Show recently sent messages");
                    System.out.println("3) Quit");

                    System.out.print("Select an option (1-3): ");
                    String choice = scanner.nextLine();

                    switch (choice) {

                        case "1":
                            // Message functionality will be added next
                            System.out.println(
                                    "Message sending will be implemented next."
                            );
                            break;

                        case "2":
                            // Required placeholder from the assignment
                            System.out.println("Coming Soon.");
                            break;

                        case "3":
                            System.out.println("Exiting QuickChat.");
                            running = false;
                            break;

                        default:
                            System.out.println(
                                    "Invalid option. Please select 1, 2 or 3."
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
                    "Registration unsuccessful. QuickChat cannot continue."
            );
        }

        scanner.close();
    }
}

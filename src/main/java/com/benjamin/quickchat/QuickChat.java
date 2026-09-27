package com.benjamin.quickchat;

import java.util.Scanner;

public class QuickChat {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("=== QuickChat Registration ===");

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

        Login user = new Login(
                username,
                password,
                cellPhoneNumber,
                firstName,
                lastName
        );

        String registrationResult = user.registerUser();
        System.out.println(registrationResult);

        if (registrationResult.equals("User successfully registered.")) {

            System.out.println();
            System.out.println("=== QuickChat Login ===");

            System.out.print("Enter your username: ");
            String loginUsername = scanner.nextLine();

            System.out.print("Enter your password: ");
            String loginPassword = scanner.nextLine();

            System.out.println(
                    user.returnLoginStatus(loginUsername, loginPassword)
            );
        }

        scanner.close();
    }
}
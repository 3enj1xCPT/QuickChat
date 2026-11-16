# QuickChat – PROG5121 Programming 1A

## Final Portfolio of Evidence | 2026

QuickChat is a Java console-based messaging application developed as part of the PROG5121 Programming 1A Portfolio of Evidence.

The application allows users to register, log in, send messages, store messages and manage their message history.

## Project Features

### Part 1 – Registration and Login
- User registration and login
- Username validation
- Password complexity validation
- International cellphone number validation

### Part 2 – Messaging
- Send, store and disregard messages
- Generate 10-digit message IDs
- Generate message hashes
- Validate recipients and message length
- Save stored messages to a JSON file

### Part 3 – Message Management
- View stored messages
- Display sender and recipient information
- Find the longest stored message
- Search messages by ID
- Search messages by recipient
- Delete stored messages using their hashes
- Generate detailed message reports
- Load stored messages from JSON

## Technologies Used

- Java 21
- Apache NetBeans
- Maven
- JUnit 5
- JSON file storage
- Git and GitHub
- GitHub Actions

## Testing

The application includes 43 automated JUnit tests covering registration, login, messaging and message management.

**Latest verified local result:**
- Tests run: 43
- Failures: 0
- Errors: 0
- Skipped: 0
- Build status: SUCCESS

GitHub Actions is configured to automatically build and test the project.

## Running the Application

1. Clone or download the GitHub repository.
2. Open the Maven project in Apache NetBeans.
3. Ensure Java 21 is installed and configured.
4. Run the `QuickChat.java` main class.
5. Follow the console instructions to register, log in and use QuickChat.

To run the automated tests, execute:

`mvn clean test`

## Project Links

**GitHub Repository:**

https://github.com/3enj1xCPT/QuickChat

**Final PoE Video Demonstration:**

https://youtu.be/KxgGco_1d1Q

## Assessment Information

**Module:** PROG5121 – Programming 1A

**Assessment:** Final Portfolio of Evidence

**Year:** 2026

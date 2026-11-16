
# QuickChat - PROG5121 Portfolio of Evidence

## Project Overview

QuickChat is a Java console-based messaging application developed for the IIE Programming 1A (PROG5121) Portfolio of Evidence.

The project was developed in three parts, covering user registration, login, message processing, JSON storage, message management and automated testing.

## Technologies Used

- Java 21
- Apache NetBeans IDE
- Apache Maven
- JUnit 5
- Git and GitHub
- GitHub Actions
- JSON file storage

## Part 1 - Registration and Login

The application allows users to register using their first name, last name, username, password and cellphone number.

The system validates:
- Username format and length
- Password complexity
- International cellphone number format
- Login credentials

Users must log in successfully before accessing QuickChat.

## Part 2 - Messaging

After logging in, users can specify how many messages they want to process.

The application supports:
- Creating messages
- Generating unique 10-digit message IDs
- Generating message hashes
- Validating recipient cellphone numbers
- Limiting messages to 250 characters
- Sending messages
- Disregarding messages
- Storing messages in a JSON file
- Displaying sent messages

## Part 3 - Message Management

Part 3 extends QuickChat with runtime message collections and a Stored Messages menu.

The application supports:
- Loading previously stored messages from JSON
- Displaying sender and recipient information
- Identifying the longest stored message
- Searching for messages using their message IDs
- Searching for messages by recipient cellphone number
- Deleting stored messages using their message hashes
- Generating a complete message report
- Displaying stored messages

Stored messages are saved in `stored_messages.json` and can be loaded again when the application restarts.

## Running the Application

1. Install Java JDK 21.
2. Open the project in Apache NetBeans.
3. Allow Maven to download the required dependencies.
4. Open `QuickChat.java` in the `com.benjamin.quickchat` package.
5. Run the application.
6. Register and log in using valid credentials.
7. Follow the console menu to use the messaging features.

The application uses a console interface and does not require a graphical user interface.

## Running Automated Tests

The project uses JUnit 5 for automated testing.

The test classes are:
- `LoginTest.java`
- `MessageTest.java`
- `Part3MessageTest.java`

To run the tests in NetBeans, right-click the QuickChat project and select Clean and Build.

Alternatively, run:

```bash
mvn clean test
```

At the time of final local verification, all 39 automated tests passed with zero failures and zero errors.

## GitHub Actions

The repository contains a Maven CI workflow at:

`.github/workflows/maven.yml`

GitHub Actions automatically builds and tests the project when changes are pushed to the configured branches.

The Part 3 workflow uses Java 21 and Maven.

## Project Structure

- `src/main/java/com/benjamin/quickchat/Login.java` - Registration and login functionality
- `src/main/java/com/benjamin/quickchat/Message.java` - Message processing, storage and management
- `src/main/java/com/benjamin/quickchat/QuickChat.java` - Main application and console menus
- `src/test/java/com/benjamin/quickchat/` - Automated JUnit tests
- `stored_messages.json` - Persisted message records
- `pom.xml` - Maven configuration
- `.github/workflows/maven.yml` - Automated build and testing workflow

## Development Branches

- `master` - Initial project development
- `part-2-messaging` - Messaging functionality
- `part-3-final-poe` - Final Portfolio of Evidence implementation

## Notes

QuickChat was developed as an academic programming project. It demonstrates Java programming, object-oriented design, validation, collections, file handling, testing and version control.

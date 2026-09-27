package com.benjamin.quickchat;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    @Test
    public void testUsernameCorrectlyFormatted() {
        Login user = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertTrue(user.checkUserName());
    }

    @Test
    public void testUsernameIncorrectlyFormatted() {
        Login user = new Login(
                "kyle!!!!!!!",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertFalse(user.checkUserName());
    }

    @Test
    public void testPasswordMeetsComplexityRequirements() {
        Login user = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertTrue(user.checkPasswordComplexity());
    }

    @Test
    public void testPasswordDoesNotMeetComplexityRequirements() {
        Login user = new Login(
                "kyl_1",
                "password",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertFalse(user.checkPasswordComplexity());
    }

    @Test
    public void testCellPhoneNumberCorrectlyFormatted() {
        Login user = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertTrue(user.checkCellPhoneNumber());
    }

    @Test
    public void testCellPhoneNumberIncorrectlyFormatted() {
        Login user = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "08966553",
                "Kyle",
                "Smith"
        );

        assertFalse(user.checkCellPhoneNumber());
    }

    @Test
    public void testSuccessfulRegistrationMessage() {
        Login user = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        String expected = "User successfully registered.";

        assertEquals(expected, user.registerUser());
    }

    @Test
    public void testInvalidUsernameRegistrationMessage() {
        Login user = new Login(
                "kyle!!!!!!!",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        String expected =
                "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";

        assertEquals(expected, user.registerUser());
    }

    @Test
    public void testInvalidPasswordRegistrationMessage() {
        Login user = new Login(
                "kyl_1",
                "password",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        String expected =
                "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";

        assertEquals(expected, user.registerUser());
    }

    @Test
    public void testInvalidCellPhoneRegistrationMessage() {
        Login user = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "08966553",
                "Kyle",
                "Smith"
        );

        String expected =
                "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.";

        assertEquals(expected, user.registerUser());
    }

    @Test
    public void testSuccessfulLogin() {
        Login user = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertTrue(
                user.loginUser("kyl_1", "Ch&&sec@ke99!")
        );
    }

    @Test
    public void testFailedLogin() {
        Login user = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertFalse(
                user.loginUser("kyl_1", "password")
        );
    }

    @Test
    public void testSuccessfulLoginStatus() {
        Login user = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        String expected =
                "Welcome Kyle, Smith it is great to see you again.";

        assertEquals(
                expected,
                user.returnLoginStatus("kyl_1", "Ch&&sec@ke99!")
        );
    }

    @Test
    public void testFailedLoginStatus() {
        Login user = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        String expected =
                "Username or password incorrect, please try again.";

        assertEquals(
                expected,
                user.returnLoginStatus("kyl_1", "password")
        );
    }
}
package com.benjamin.quickchat;

public class Login {

    private String username;
    private String password;
    private String cellPhoneNumber;
    private String firstName;
    private String lastName;

    public Login(String username, String password, String cellPhoneNumber,
                 String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public boolean checkUserName() {
        return username.contains("_") && username.length() <= 5;
    }

    public boolean checkPasswordComplexity() {
        boolean hasCapitalLetter = password.matches(".*[A-Z].*");
        boolean hasNumber = password.matches(".*[0-9].*");
        boolean hasSpecialCharacter = password.matches(".*[^a-zA-Z0-9].*");

        return password.length() >= 8
                && hasCapitalLetter
                && hasNumber
                && hasSpecialCharacter;
    }

    /*
     * The regular expression used for cellphone number validation
     * was developed with reference to Oracle's Java regular
     * expression documentation (Oracle, 2026).
     */
    public boolean checkCellPhoneNumber() {
        return cellPhoneNumber.matches("^\\+27\\d{9}$");
    }

    public String registerUser() {

        if (!checkUserName()) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }

        if (!checkPasswordComplexity()) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }

        if (!checkCellPhoneNumber()) {
            return "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.";
        }

        return "User successfully registered.";
    }

    public boolean loginUser(String enteredUsername, String enteredPassword) {
        return enteredUsername.equals(username)
                && enteredPassword.equals(password);
    }

    public String returnLoginStatus(String enteredUsername, String enteredPassword) {

        if (loginUser(enteredUsername, enteredPassword)) {
            return "Welcome " + firstName + ", " + lastName
                    + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }

    /*
     * Reference List
     *
     * Oracle. 2026. String (Java SE 26 & JDK 26).
     * Available at:
     * https://docs.oracle.com/en/java/javase/26/docs/api/java.base/java/lang/String.html
     */
}
package com.blukers.automation.testdata.model;

public class LoginData {

    private String username;
    private String password;

    // Jackson requires a no-arg constructor
    public LoginData() {
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
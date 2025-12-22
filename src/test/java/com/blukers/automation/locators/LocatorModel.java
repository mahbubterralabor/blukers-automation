package com.blukers.automation.locators;

public class LocatorModel {

    private String type;
    private String value;

    // Required by Jackson
    public LocatorModel() {
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}

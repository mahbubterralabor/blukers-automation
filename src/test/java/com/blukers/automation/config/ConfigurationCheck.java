package com.blukers.automation.config;

public class ConfigurationCheck {
    public static void main(String[] args) {

        FrameworkConfig config = ConfigLoader.load();
        ConfigValidator.validate(config);

        System.out.println("Configuration loaded successfully");
        System.out.println("Platform: " + config.getPlatform());
    }
}

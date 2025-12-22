package com.blukers.automation.config.appium;

public final class AppiumConfig {

    private final String serverUrl;
    private final int commandTimeoutSeconds;

    public AppiumConfig(String serverUrl, int commandTimeoutSeconds) {
        this.serverUrl = serverUrl;
        this.commandTimeoutSeconds = commandTimeoutSeconds;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public int getCommandTimeoutSeconds() {
        return commandTimeoutSeconds;
    }
}

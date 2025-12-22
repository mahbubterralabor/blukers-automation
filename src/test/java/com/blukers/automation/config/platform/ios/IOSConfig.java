package com.blukers.automation.config.platform.ios;

public final class IOSConfig {

    private final String bundleId;

    public IOSConfig(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getBundleId() {
        return bundleId;
    }
}

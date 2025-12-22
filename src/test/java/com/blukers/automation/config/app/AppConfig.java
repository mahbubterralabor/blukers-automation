package com.blukers.automation.config.app;

public final class AppConfig {

    private final String appPath;
    private final boolean noReset;

    public AppConfig(String appPath, boolean noReset) {
        this.appPath = appPath;
        this.noReset = noReset;
    }

    public String getAppPath() {
        return appPath;
    }

    public boolean isNoReset() {
        return noReset;
    }
}

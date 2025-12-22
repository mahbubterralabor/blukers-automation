package com.blukers.automation.config.platform.android;

public final class AndroidConfig {

    private final String appPackage;
    private final String appActivity;
    private final String deviceUdid;

    public AndroidConfig(String appPackage, String appActivity, String deviceUdid) {
        this.appPackage = appPackage;
        this.appActivity = appActivity;
        this.deviceUdid = deviceUdid;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public String getAppActivity() {
        return appActivity;
    }

    public String getDeviceUdid() {
        return deviceUdid;
    }
}

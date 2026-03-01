package com.blukers.automation.config.platform.ios;

public class IOSConfig {

    // AUT bundle id (your app under test)
    private String bundleId;

    // Real device details
    private String deviceName;
    private String platformVersion;
    private String udid;

    // Xcode signing / WDA config
    private String xcodeOrgId;
    private String xcodeSigningId;
    private String updatedWdaBundleId;
    private boolean useNewWda = false;

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public void setPlatformVersion(String platformVersion) {
        this.platformVersion = platformVersion;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getXcodeOrgId() {
        return xcodeOrgId;
    }

    public void setXcodeOrgId(String xcodeOrgId) {
        this.xcodeOrgId = xcodeOrgId;
    }

    public String getXcodeSigningId() {
        return xcodeSigningId;
    }

    public void setXcodeSigningId(String xcodeSigningId) {
        this.xcodeSigningId = xcodeSigningId;
    }

    public String getUpdatedWdaBundleId() {
        return updatedWdaBundleId;
    }

    public void setUpdatedWdaBundleId(String updatedWdaBundleId) {
        this.updatedWdaBundleId = updatedWdaBundleId;
    }

    public boolean isUseNewWda() {
        return useNewWda;
    }

    public void setUseNewWda(boolean useNewWda) {
        this.useNewWda = useNewWda;
    }
}
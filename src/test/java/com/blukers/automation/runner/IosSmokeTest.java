package com.blukers.automation.runner;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.options.BaseOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

import java.net.URL;
import java.time.Duration;

public class IosSmokeTest {

    @Test
    public void openSettings() throws Exception {
        BaseOptions<?> options = new BaseOptions<>()
                .setPlatformName("iOS")
                .amend("appium:automationName", "XCUITest")
                .amend("appium:deviceName", "iPhone 16 Pro")
                .amend("appium:platformVersion", "18.3")
                .amend("appium:udid", "E059BC30-807B-496E-818F-11FAE1B0D933")
                .amend("appium:noReset", true)
                .amend("appium:newCommandTimeout", 120)
                // Launch Settings (proves session + WDA works)
                .amend("appium:bundleId", "com.apple.Preferences");

        IOSDriver driver = new IOSDriver(new URL("http://127.0.0.1:4723/"), options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));

        // Just keep it simple: if session created, test passes
        System.out.println("Session started: " + driver.getSessionId());

        driver.quit();
    }
}
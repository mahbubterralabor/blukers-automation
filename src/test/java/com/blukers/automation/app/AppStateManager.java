package com.blukers.automation.app;

import com.blukers.automation.config.FrameworkConfig;
import com.blukers.automation.config.Platform;
import com.blukers.automation.driver.DriverManager;
import com.blukers.automation.util.Log;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.Map;

public final class AppStateManager {

    private static final Logger log = Log.get(AppStateManager.class);

    // Appium queryAppState values:
    // 0 = not installed
    // 1 = not running
    // 2 = running in background (suspended)
    // 3 = running in background
    // 4 = running in foreground
    private static final int STATE_FOREGROUND = 4;
    private static final int STATE_BACKGROUND = 3;
    private static final int STATE_BACKGROUND_SUSPENDED = 2;

    private AppStateManager() {}

    /** Relaunch = terminate + activate + verify app is in FOREGROUND. */
    public static void relaunch(FrameworkConfig config) {
        String appId = resolveAppId(config);

        terminate(config);
        // small buffer for OS to settle (especially on real devices)
        sleep(500);

        activate(config);

        // Sometimes it launches but stays background; force foreground again if needed
        int state = queryState(appId);
        log.info("App state after activate: {} (appId={})", state, appId);

        if (state == STATE_BACKGROUND || state == STATE_BACKGROUND_SUSPENDED) {
            log.warn("App is running but not foreground. Forcing foreground again (appId={})", appId);
            activate(config);
        }

        waitForState(appId, STATE_FOREGROUND, Duration.ofSeconds(20));
    }

    /** Bring app to foreground (Android uses startActivity; iOS uses activateApp). */
    public static void activate(FrameworkConfig config) {
        if (config == null) throw new IllegalArgumentException("FrameworkConfig is null");
        AppiumDriver driver = DriverManager.getDriver();
        String appId = resolveAppId(config);

        if (config.getPlatform() == Platform.ANDROID) {
            // Use activateApp instead of mobile: startActivity for better stability
            // This avoids the "No intent supplied" ADB error
            try {
                driver.executeScript("mobile: activateApp", Map.of("appId", appId));
            } catch (Exception e) {
                log.warn("mobile: activateApp failed, falling back to shell start");
                String activity = config.getAndroid().getAppActivity();
                driver.executeScript("mobile: shell", Map.of(
                        "command", "am",
                        "args", java.util.List.of("start", "-n", appId + "/" + activity)
                ));
            }
        } else {
            driver.executeScript("mobile: activateApp", Map.of("appId", appId));
        }
    }

    /** Terminate app (with Android fallback force-stop if needed). */
    public static void terminate(FrameworkConfig config) {
        String appId = resolveAppId(config);
        AppiumDriver driver = DriverManager.getDriver();

        try {
            driver.executeScript("mobile: terminateApp", Map.of("appId", appId));
        } catch (Exception primary) {
            log.warn("terminateApp failed. Trying Android force-stop fallback. appId={}. Error={}",
                    appId, primary.getMessage());

            // Android fallback: adb am force-stop <package>
            if (config.getPlatform() == Platform.ANDROID && driver instanceof AndroidDriver) {
                try {
                    driver.executeScript("mobile: shell", Map.of(
                            "command", "am",
                            "args", java.util.List.of("force-stop", appId)
                    ));
                } catch (Exception fallback) {
                    throw new RuntimeException("Failed to terminate app (both terminateApp and force-stop). appId=" + appId, fallback);
                }
            } else {
                throw new RuntimeException("Failed to terminate app. appId=" + appId, primary);
            }
        }

        // Wait until app is not foreground anymore (or stopped)
        waitUntilNotForeground(appId, Duration.ofSeconds(10));
    }

    /** Query current app state via Appium. */
    public static int queryState(String appId) {
        AppiumDriver driver = DriverManager.getDriver();
        Object result = driver.executeScript("mobile: queryAppState", Map.of("appId", appId));

        if (result instanceof Number n) {
            return n.intValue();
        }
        // Some servers return String
        if (result instanceof String s) {
            try {
                return Integer.parseInt(s.trim());
            } catch (Exception ignored) {}
        }

        throw new IllegalStateException("Unexpected queryAppState response: " + result);
    }

    /** Wait for an exact expected state (e.g., foreground=4). */
    public static void waitForState(String appId, int expectedState, Duration timeout) {
        long end = System.currentTimeMillis() + timeout.toMillis();

        int last = -1;
        while (System.currentTimeMillis() < end) {
            int state = queryState(appId);
            last = state;

            if (state == expectedState) {
                return;
            }

            sleep(300);
        }

        throw new IllegalStateException(
                "App did not reach expected state. expected=" + expectedState +
                        ", actual=" + last + ", appId=" + appId
        );
    }

    /** Wait until app is NOT in foreground (useful after terminate). */
    private static void waitUntilNotForeground(String appId, Duration timeout) {
        long end = System.currentTimeMillis() + timeout.toMillis();

        int last = -1;
        while (System.currentTimeMillis() < end) {
            int state = queryState(appId);
            last = state;

            if (state != STATE_FOREGROUND) {
                return;
            }

            sleep(300);
        }

        log.warn("App still appears foreground after terminate wait. lastState={} appId={}", last, appId);
    }

    /** Resolve appId from config (Android package or iOS bundle id). */
    private static String resolveAppId(FrameworkConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("FrameworkConfig is null");
        }

        return switch (config.getPlatform()) {
            case ANDROID -> {
                String pkg = config.getAndroid().getAppPackage();
                if (pkg == null || pkg.isBlank()) {
                    throw new IllegalStateException("android.app.package is missing in config");
                }
                yield pkg;
            }
            case IOS -> {
                String bundleId = config.getIos().getBundleId();
                if (bundleId == null || bundleId.isBlank()) {
                    throw new IllegalStateException("ios.bundle.id is missing in config");
                }
                yield bundleId;
            }
        };
    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
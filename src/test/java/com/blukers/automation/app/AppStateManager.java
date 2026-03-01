package com.blukers.automation.app;

import com.blukers.automation.config.FrameworkConfig;
import com.blukers.automation.config.Platform;
import com.blukers.automation.driver.DriverManager;
import com.blukers.automation.util.Log;
import io.appium.java_client.AppiumDriver;
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
    private static final int NOT_INSTALLED = 0;
    private static final int NOT_RUNNING = 1;
    private static final int BG_SUSPENDED = 2;
    private static final int BG = 3;
    private static final int FG = 4;

    private static final Duration DEFAULT_FOREGROUND_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration DEFAULT_RELAUNCH_TIMEOUT = Duration.ofSeconds(20);

    public enum HealthStatus {
        OK,
        RELAUNCH_RECOMMENDED,
        SESSION_INVALID
    }

    private AppStateManager() {}

    /**
     * Health check used by Hooks to decide relaunch vs ensureForeground vs driver restart.
     * - If session invalid -> SESSION_INVALID
     * - If app not installed -> RELAUNCH_RECOMMENDED (will fail later anyway, but keeps flow consistent)
     * - If app not running -> RELAUNCH_RECOMMENDED
     * - If background -> OK (ensureForeground is enough)
     * - If foreground -> OK
     */
    public static HealthStatus checkHealth(FrameworkConfig config) {
        if (!DriverManager.hasDriver()) {
            return HealthStatus.SESSION_INVALID;
        }

        AppiumDriver driver = DriverManager.getDriver();
        if (driver.getSessionId() == null) {
            return HealthStatus.SESSION_INVALID;
        }

        String appId = resolveAppId(config);

        try {
            int state = queryState(appId);
            log.info("Health check appState={} (appId={})", state, appId);

            if (state == NOT_INSTALLED) return HealthStatus.RELAUNCH_RECOMMENDED;
            if (state == NOT_RUNNING) return HealthStatus.RELAUNCH_RECOMMENDED;

            // BG / BG_SUSPENDED / FG are recoverable by ensureForeground()
            return HealthStatus.OK;

        } catch (Exception e) {
            // If queryAppState fails, it is usually session instability or driver issue
            log.warn("Health check failed (queryAppState). Treating as SESSION_INVALID. err={}", e.getMessage());
            return HealthStatus.SESSION_INVALID;
        }
    }

    /**
     * Conditional foregrounding:
     * - If already foreground -> do nothing
     * - Else activate -> wait for FG
     */
    public static void ensureForeground(FrameworkConfig config) {
        ensureForeground(config, DEFAULT_FOREGROUND_TIMEOUT);
    }

    public static void ensureForeground(FrameworkConfig config, Duration timeout) {
        requireDriverReady();

        String appId = resolveAppId(config);

        int state = safeQueryState(appId);
        log.info("ensureForeground: currentState={} (appId={})", state, appId);

        if (state == FG) return;

        activate(appId);

        waitForState(appId, FG, timeout);

        log.info("ensureForeground: finalState={} (appId={})", safeQueryState(appId), appId);
    }

    /**
     * Straight relaunch (no conditions inside):
     * terminate -> activate -> wait FG
     */
    public static void relaunch(FrameworkConfig config) {
        relaunch(config, DEFAULT_RELAUNCH_TIMEOUT);
    }

    public static void relaunch(FrameworkConfig config, Duration timeout) {
        requireDriverReady();

        String appId = resolveAppId(config);

        terminate(appId);
        sleep(350);

        activate(appId);

        waitForState(appId, FG, timeout);

        log.info("relaunch: finalState={} (appId={})", safeQueryState(appId), appId);
    }

    /* ===================== Low-level actions ===================== */

    private static void activate(String appId) {
        AppiumDriver driver = DriverManager.getDriver();
        driver.executeScript("mobile: activateApp", Map.of("appId", appId));
    }

    private static void terminate(String appId) {
        AppiumDriver driver = DriverManager.getDriver();
        driver.executeScript("mobile: terminateApp", Map.of("appId", appId));
    }

    private static int queryState(String appId) {
        AppiumDriver driver = DriverManager.getDriver();
        Object result = driver.executeScript("mobile: queryAppState", Map.of("appId", appId));

        if (result instanceof Number n) return n.intValue();
        if (result instanceof String s) return Integer.parseInt(s.trim());

        throw new IllegalStateException("Unexpected queryAppState response: " + result);
    }

    private static int safeQueryState(String appId) {
        try {
            return queryState(appId);
        } catch (Exception e) {
            log.warn("queryAppState failed: appId={} err={}", appId, e.getMessage());
            return NOT_RUNNING;
        }
    }

    private static void waitForState(String appId, int expected, Duration timeout) {
        long end = System.currentTimeMillis() + timeout.toMillis();
        int last = -1;

        while (System.currentTimeMillis() < end) {
            last = safeQueryState(appId);
            if (last == expected) return;
            sleep(200);
        }

        throw new IllegalStateException(
                "App did not reach expected state. expected=" + expected +
                        ", actual=" + last + ", appId=" + appId
        );
    }

    private static void requireDriverReady() {
        if (!DriverManager.hasDriver()) {
            throw new IllegalStateException("Driver is not initialized. Start driver before using AppStateManager.");
        }
        AppiumDriver driver = DriverManager.getDriver();
        if (driver.getSessionId() == null) {
            throw new IllegalStateException("Driver session is null (invalid session). Restart driver session.");
        }
    }

    private static String resolveAppId(FrameworkConfig config) {
        if (config == null) throw new IllegalArgumentException("FrameworkConfig is null");

        Platform platform = config.getPlatform();
        return switch (platform) {
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
        try { Thread.sleep(ms); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
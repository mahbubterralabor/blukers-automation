package com.blukers.automation.locators;

import com.blukers.automation.config.Platform;
import com.blukers.automation.util.Log;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
//import java.util.logging.Logger;

public final class LocatorLoader {


    private static final Logger log = Log.get(LocatorLoader.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Cache structure:
     *   ANDROID -> HomePage -> Map<key, LocatorModel>
     *   IOS     -> HomePage -> Map<key, LocatorModel>
     */
    private static final Map<Platform, Map<String, Map<String, LocatorModel>>> CACHE =
            new ConcurrentHashMap<>();

    private LocatorLoader() {
        // utility class
    }

    public static Map<String, LocatorModel> load(Platform platform, String pageName) {

        return CACHE
                .computeIfAbsent(platform, p -> new ConcurrentHashMap<>())
                .computeIfAbsent(pageName, p -> loadFromFile(platform, pageName));
    }

    private static Map<String, LocatorModel> loadFromFile(Platform platform, String pageName) {

        String resourcePath = String.format(
                "locators/%s/%s.json",
                platform.name().toLowerCase(),
                pageName
        );

        try (InputStream is = LocatorLoader.class
                .getClassLoader()
                .getResourceAsStream(resourcePath)) {

            if (is == null) {
                throw new IllegalStateException(
                        "Locator file not found: " + resourcePath
                );
            }

            return MAPPER.readValue(
                    is,
                    new TypeReference<Map<String, LocatorModel>>() {}
            );

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to load locator file: " + resourcePath,
                    e
            );
        }
    }
}

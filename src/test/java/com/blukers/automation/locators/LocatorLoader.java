package com.blukers.automation.locators;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class LocatorLoader {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    // Cache: platform/pageName -> locator map
    private static final Map<String, Map<String, LocatorModel>> CACHE =
            new ConcurrentHashMap<>();

    private LocatorLoader() {
        // utility class
    }

    public static Map<String, LocatorModel> load(
            String platform,
            String pageName
    ) {
        String cacheKey = platform + "/" + pageName;

        return CACHE.computeIfAbsent(cacheKey, key -> {
            String resourcePath = String.format(
                    "locators/%s/%s.json",
                    platform.toLowerCase(),
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

                Map<String, LocatorModel> locators =
                        OBJECT_MAPPER.readValue(
                                is,
                                new TypeReference<>() {}
                        );

                return Collections.unmodifiableMap(locators);

            } catch (Exception e) {
                throw new RuntimeException(
                        "Failed to load locators for " + cacheKey,
                        e
                );
            }
        });
    }
}

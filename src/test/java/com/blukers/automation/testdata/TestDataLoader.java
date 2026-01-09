package com.blukers.automation.testdata;

import com.blukers.automation.config.Platform;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class TestDataLoader {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Map-based cache:
     * ANDROID -> file -> key -> Map<String, String>
     */
    private static final Map<Platform, Map<String, Map<String, Map<String, String>>>> MAP_CACHE =
            new ConcurrentHashMap<>();

    /**
     * Typed cache:
     * ANDROID -> file -> key -> T
     */
    private static final Map<Platform, Map<String, Map<String, Object>>> TYPED_CACHE =
            new ConcurrentHashMap<>();

    private TestDataLoader() {
        // utility class
    }

    // ------------------------------------------------
    // MAP-BASED ACCESS (Phase 7.1 – still supported)
    // ------------------------------------------------
    public static Map<String, String> get(
            Platform platform,
            String fileName,
            String dataKey
    ) {

        Map<String, Map<String, String>> fileData =
                MAP_CACHE
                        .computeIfAbsent(platform, p -> new ConcurrentHashMap<>())
                        .computeIfAbsent(fileName, f -> loadMapFile(platform, fileName));

        Map<String, String> data = fileData.get(dataKey);

        if (data == null) {
            throw new IllegalArgumentException("Test data key not found: " + dataKey);
        }

        return data;
    }

    private static Map<String, Map<String, String>> loadMapFile(
            Platform platform,
            String fileName
    ) {

        String path = String.format(
                "testdata/%s/%s.json",
                platform.name().toLowerCase(),
                fileName
        );

        try (InputStream is = TestDataLoader.class
                .getClassLoader()
                .getResourceAsStream(path)) {

            if (is == null) {
                throw new IllegalStateException("Test data file not found: " + path);
            }

            return MAPPER.readValue(
                    is,
                    new TypeReference<Map<String, Map<String, String>>>() {}
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to load test data: " + path, e);
        }
    }

    // ------------------------------------------------
    // TYPED ACCESS (Phase 7.2 – preferred)
    // ------------------------------------------------
    @SuppressWarnings("unchecked")
    public static <T> T getTyped(
            Platform platform,
            String fileName,
            String dataKey,
            Class<T> type
    ) {

        Map<String, Object> fileCache =
                TYPED_CACHE
                        .computeIfAbsent(platform, p -> new ConcurrentHashMap<>())
                        .computeIfAbsent(fileName, f -> loadTypedFile(platform, fileName, type));

        Object data = fileCache.get(dataKey);

        if (data == null) {
            throw new IllegalArgumentException("Test data key not found: " + dataKey);
        }

        return (T) data;
    }

    private static <T> Map<String, Object> loadTypedFile(
            Platform platform,
            String fileName,
            Class<T> type
    ) {

        String path = String.format(
                "testdata/%s/%s.json",
                platform.name().toLowerCase(),
                fileName
        );

        try (InputStream is = TestDataLoader.class
                .getClassLoader()
                .getResourceAsStream(path)) {

            if (is == null) {
                throw new IllegalStateException("Test data file not found: " + path);
            }

            Map<String, T> parsed =
                    MAPPER.readValue(
                            is,
                            MAPPER.getTypeFactory()
                                    .constructMapType(Map.class, String.class, type)
                    );

            return new ConcurrentHashMap<>(parsed);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load test data: " + path, e);
        }
    }
}
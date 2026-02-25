package com.blukers.automation.testdata;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Loads test data JSON files and maps specific dataset nodes into typed POJOs.
 *
 * Example usage:
 *   LoginData data =
 *       TestDataLoader.load("login", "login_valid", LoginData.class);
 *
 * JSON structure:
 * {
 *   "login_valid": { ... },
 *   "login_invalid_password": { ... }
 * }
 */
public final class TestDataLoader {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    // Cache per file path (e.g., testdata/login.json)
    private static final Map<String, JsonNode> CACHE = new ConcurrentHashMap<>();

    private TestDataLoader() {
        // utility class
    }

    /**
     * Loads a dataset from a JSON file and maps it into a POJO.
     *
     * @param fileBaseName name of file without extension (e.g., "login")
     * @param dataKey      dataset key (e.g., "login_valid")
     * @param clazz        target class
     */
    public static <T> T load(String fileBaseName, String dataKey, Class<T> clazz) {

        if (fileBaseName == null || fileBaseName.isBlank()) {
            throw new IllegalArgumentException("fileBaseName must not be blank");
        }

        if (dataKey == null || dataKey.isBlank()) {
            throw new IllegalArgumentException("dataKey must not be blank");
        }

        if (clazz == null) {
            throw new IllegalArgumentException("Target class must not be null");
        }

        String resourcePath = String.format("testdata/%s.json", fileBaseName);

        JsonNode root = CACHE.computeIfAbsent(resourcePath, TestDataLoader::loadRootNode);

        JsonNode datasetNode = root.get(dataKey);

        if (datasetNode == null || datasetNode.isNull()) {
            throw new IllegalStateException(
                    "Dataset key not found in file: " + resourcePath +
                            ", key: " + dataKey
            );
        }

        try {
            return MAPPER.treeToValue(datasetNode, clazz);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to map dataset. file=" + resourcePath +
                            ", key=" + dataKey +
                            ", class=" + clazz.getSimpleName(),
                    e
            );
        }
    }

    private static JsonNode loadRootNode(String resourcePath) {

        try (InputStream is =
                     TestDataLoader.class
                             .getClassLoader()
                             .getResourceAsStream(resourcePath)) {

            if (is == null) {
                throw new IllegalStateException(
                        "Test data file not found: " + resourcePath
                );
            }

            return MAPPER.readTree(is);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to load test data file: " + resourcePath,
                    e
            );
        }
    }
}
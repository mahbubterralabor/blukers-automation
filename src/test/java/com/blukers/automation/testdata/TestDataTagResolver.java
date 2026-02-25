package com.blukers.automation.testdata;

import io.cucumber.java.Scenario;

import java.util.Optional;

public final class TestDataTagResolver {

    private static final String PREFIX = "@data_";

    private TestDataTagResolver() {}

    public static Optional<String> resolveDataKey(Scenario scenario) {
        return scenario.getSourceTagNames().stream()
                .filter(t -> t.startsWith(PREFIX))
                .map(t -> t.substring(PREFIX.length()))   // "login_valid"
                .findFirst();
    }
}
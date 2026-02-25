package com.blukers.automation.testdata;

public final class TestDataContext {

    private static final ThreadLocal<String> DATA_KEY = new ThreadLocal<>();

    private TestDataContext() {}

    public static void setDataKey(String key) {
        DATA_KEY.set(key);
    }

    public static String getDataKey() {
        return DATA_KEY.get();
    }

    public static void clear() {
        DATA_KEY.remove();
    }
}
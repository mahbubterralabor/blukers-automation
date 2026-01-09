package com.blukers.automation.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Log {

    private Log() {
    }

    public static Logger get(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}
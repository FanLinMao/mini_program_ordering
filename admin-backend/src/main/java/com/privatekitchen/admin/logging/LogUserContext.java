package com.privatekitchen.admin.logging;

import org.slf4j.MDC;

public final class LogUserContext {

    public static final String MDC_KEY = "loginUser";

    private LogUserContext() {
    }

    public static void bind(String username) {
        if (username == null || username.isBlank()) {
            return;
        }
        MDC.put(MDC_KEY, username.trim());
    }

    public static void clear() {
        MDC.remove(MDC_KEY);
    }
}

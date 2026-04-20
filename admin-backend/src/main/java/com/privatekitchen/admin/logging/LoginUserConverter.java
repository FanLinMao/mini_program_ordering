package com.privatekitchen.admin.logging;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class LoginUserConverter extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent event) {
        String loginUser = event.getMDCPropertyMap().get(LogUserContext.MDC_KEY);
        if (loginUser != null && !loginUser.isBlank()) {
            return loginUser;
        }
        return event.getThreadName();
    }
}

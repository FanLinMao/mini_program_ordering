package com.privatekitchen.admin.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class LoginUserMdcFilter extends OncePerRequestFilter {

    private static final String HEADER_LOGIN_USER = "X-Login-User";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String username = resolveUsername(request);
        LogUserContext.bind(username);
        try {
            filterChain.doFilter(request, response);
        } finally {
            LogUserContext.clear();
        }
    }

    private String resolveUsername(HttpServletRequest request) {
        String username = request.getHeader(HEADER_LOGIN_USER);
        if (username != null && !username.isBlank()) {
            return username;
        }
        return request.getParameter("username");
    }
}

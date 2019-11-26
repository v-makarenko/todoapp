package com.example.todoapp.config.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import liquibase.util.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private static final String SESSION_ID_COOKIE = "JSESSIONID";

    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication auth
    )throws IOException, ServletException {
        String contentType = request.getHeader(HttpHeaders.CONTENT_TYPE);
        if (StringUtils.isNotEmpty(contentType) && contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
            response.setStatus(HttpStatus.OK.value());
            HttpSession session = request.getSession(true);
            Cookie cookie = new Cookie(SESSION_ID_COOKIE, session.getId());
            cookie.setPath(request.getContextPath());
            cookie.setDomain(request.getServerName());
            response.addCookie(cookie);
            response.getWriter().flush();
        } else {
            super.onAuthenticationSuccess(request, response, auth);
        }
    }
}
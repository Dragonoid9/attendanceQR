package com.cosmotechintl.AttendanceSystem.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        Map<String, Object> data = new HashMap<>();
        data.put("timestamp", Calendar.getInstance().getTime());
        data.put("exception", exception.getMessage());
        if (exception.getMessage().equalsIgnoreCase("Maximum sessions of 1 for this principal exceeded")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Session is Already Active, Please contact admin to reset session");
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, exception.getMessage());
        }
    }
}
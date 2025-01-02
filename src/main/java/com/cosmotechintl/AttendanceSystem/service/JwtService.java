package com.cosmotechintl.AttendanceSystem.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface JwtService {

    String extractUsername(String token);

    boolean validateToken(String token, UserDetails userDetails);

    boolean validateRefreshToken(String token);

    String generateToken(String username, List<String> roles);

    String generateRefreshToken(String username);

    List<String> extractRolesFromUsername(String username);
}

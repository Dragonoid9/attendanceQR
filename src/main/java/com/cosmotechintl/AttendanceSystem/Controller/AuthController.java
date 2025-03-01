package com.cosmotechintl.AttendanceSystem.Controller;


import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.LoginRequestDTO;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.ApiResponse;
import com.cosmotechintl.AttendanceSystem.exception.TokenNotFoundException;
import com.cosmotechintl.AttendanceSystem.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ApiResponse<?> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return authService.loginUser(loginRequestDTO);
    }

    @PostMapping("/refreshToken")
    public ApiResponse<?> refreshToken(@RequestHeader("Authorization") String authHeader) {

        String refreshToken = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            refreshToken = authHeader.substring(7);  // Remove "Bearer " from the start of the string
        } else {
            throw new TokenNotFoundException("Refresh token is missing or invalid.");
        }
        log.info("refreshToken: " + refreshToken);
        return authService.generateRefreshToken(refreshToken);
    }

    @PostMapping("/logout")
    public ApiResponse<?> logout(@RequestHeader("Authorization") String authHeader) {
        String accessToken = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            accessToken = authHeader.substring(7);  // Remove "Bearer "
        } else {
            throw new TokenNotFoundException("Access token is missing or invalid.");
        }
        return authService.logoutUser(accessToken);
    }

    @PreAuthorize("hasRole('SUPER ADMIN')")
    @GetMapping("/getAllRefreshToken")
    public ApiResponse<?> getAllRefreshToken() {
        return authService.getAllRefreshTokens();
    }

}



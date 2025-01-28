package com.cosmotechintl.AttendanceSystem.service.impl;


import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.LoginRequestDTO;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.ApiResponse;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.AuthResponseDTO;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.LoginResponseDTO;
import com.cosmotechintl.AttendanceSystem.entity.AuthToken;
import com.cosmotechintl.AttendanceSystem.entity.UserInfo;
import com.cosmotechintl.AttendanceSystem.exception.ResourceNotFoundException;
import com.cosmotechintl.AttendanceSystem.repository.AuthTokenRepository;
import com.cosmotechintl.AttendanceSystem.repository.UserInfoRepository;
import com.cosmotechintl.AttendanceSystem.service.AuthService;
import com.cosmotechintl.AttendanceSystem.service.JwtService;
import com.cosmotechintl.AttendanceSystem.utility.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthTokenRepository authTokenRepository;
    private final UserInfoRepository userInfoRepository;

    @Override
    public ApiResponse<?> loginUser(LoginRequestDTO loginRequestDTO) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            String accessToken = jwtService.generateToken(loginRequestDTO.getUsername(), roles);
            String refreshToken = jwtService.generateRefreshToken(loginRequestDTO.getUsername());
            AuthToken authToken = AuthToken.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .issuedDate(LocalDateTime.now())
                    .isActive(false)
                    .userInfo((UserInfo) userDetails)
                    .build();
            authTokenRepository.save(authToken);
            LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
            return ResponseUtil.getSuccessResponse(loginResponseDTO, "Login Successful");
        } catch (DataAccessException e) {
            return ResponseUtil.getValidationErrorResponse("Something went wrong. " + "Please contact support.");
        } catch (AuthenticationException e) {
            return ResponseUtil.getValidationErrorResponse("The username or password is incorrect. Please try again.");
        } catch (Exception e) {
            return ResponseUtil.getErrorResponse(e, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @Override
    public ApiResponse<?> generateRefreshToken(String refreshToken) {
        try {
            if (refreshToken == null || refreshToken.trim().isEmpty()) {
                return ResponseUtil.getValidationErrorResponse("Refresh token is missing or invalid.");
            }
            log.info("Before passing the refresh token for the extraction of username");
            if (!jwtService.validateRefreshToken(refreshToken)) {
                return ResponseUtil.getValidationErrorResponse("Invalid or expired access token.");
            }
            log.info("Validation of refresh token is successful.");
            String username = jwtService.extractUsername(refreshToken);
            List<String> roles = jwtService.extractRolesFromUsername(username);

            authTokenRepository.setIsActiveTrue(refreshToken);
            log.info("Used column value is changed.");

            String accessToken = jwtService.generateToken(username, roles);
            String refreshTokenRefresh = jwtService.generateRefreshToken(username);

            UserInfo userDetails = userInfoRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found" + username));

            AuthToken authToken = AuthToken.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshTokenRefresh)
                    .issuedDate(LocalDateTime.now())
                    .isActive(false)
                    .userInfo((UserInfo) userDetails)
                    .build();
            authTokenRepository.save(authToken);

            log.info(" After the repository saved of the authToken.");

            AuthResponseDTO authResponseDTO = AuthResponseDTO.
                    builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshTokenRefresh)
                    .build();

            return ResponseUtil.getSuccessResponse(authResponseDTO, "Successfully Refreshed.");
        } catch (Exception e) {
            return ResponseUtil.getErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ApiResponse<?> logoutUser(String accessToken) {
        try {
            if (accessToken == null || accessToken.trim().isEmpty()) {
                return ResponseUtil.getValidationErrorResponse("Access token is missing or invalid.");
            }
            // Extract username and device ID from the token
            String username = jwtService.extractUsername(accessToken);

            // Validate token and device ID
            AuthToken authToken = authTokenRepository.findByAccessToken(accessToken)
                    .orElseThrow(() -> new ResourceNotFoundException("Token not found or already logged out."));

            // Mark token as used/inactive
            authTokenRepository.setIsActiveTrue(authToken.getRefreshToken()); // Assuming true indicates the token is invalidated
            SecurityContextHolder.clearContext();
            return ResponseUtil.getSuccessResponse(null, "Logout successful.");
        } catch (Exception e) {
            return ResponseUtil.getErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ApiResponse<?> getAllRefreshTokens() {
        log.info("Getting all refresh tokens.");
        List<AuthToken> authTokens = authTokenRepository.findAll();
        List<AuthResponseDTO> authResponses = new ArrayList<>();
        for (AuthToken authToken : authTokens) {
            AuthResponseDTO authResponseDTO = new AuthResponseDTO();
            authResponseDTO.setAccessToken(authToken.getAccessToken());
            authResponseDTO.setRefreshToken(authToken.getRefreshToken());
            authResponses.add(authResponseDTO);
        }
        return ResponseUtil.getSuccessResponse(authResponses, "Successfully retrieved all refresh tokens.");
    }

    @Scheduled(cron = "0 0 0 L * ? ")
    private void dropAuthTokenTable() {
        try {
            authTokenRepository.deleteAll();
            log.info("All records from authToken table deleted at: {}", LocalDateTime.now());
        } catch (Exception e) {
            log.error("Error while deleting records from authToken table", e);
        }
    }
}

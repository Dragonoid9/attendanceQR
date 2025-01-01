package com.cosmotechintl.AttendanceSystem.service;


import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.LoginRequestDTO;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.ApiResponse;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.AuthResponseDTO;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.LoginResponseDTO;
import com.cosmotechintl.AttendanceSystem.entity.AuthToken;
import com.cosmotechintl.AttendanceSystem.entity.UserInfo;
import com.cosmotechintl.AttendanceSystem.repository.AuthTokenRepository;
import com.cosmotechintl.AttendanceSystem.repository.UserInfoRepository;
import com.cosmotechintl.AttendanceSystem.utility.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImp implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthTokenRepository authTokenRepository;
    private final UserInfoRepository userRepository;


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
            log.info("Access Token: {} Refresh Token: {}", accessToken, refreshToken);
            AuthToken authToken = AuthToken.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .issuedDate(LocalDateTime.now())
                    .isActive(false)
                    .userInfo((UserInfo)userDetails)
                    .build();
            log.info("Something after the builder"+authToken.toString());
            authTokenRepository.save(authToken);
            log.info("After the repository saved of the authToken.");
            LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
            return ResponseUtil.getSuccessResponse(loginResponseDTO, "Login Successful");
        }catch (DataAccessException e) {
            return ResponseUtil.getValidationErrorResponse("Something went wrong. "+"Please contact support.");
        }catch (AuthenticationException e) {
                return ResponseUtil.getValidationErrorResponse("Bad Credentials");
        }catch (Exception e) {
            return ResponseUtil.getErrorResponse(e, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }


//    @Override
//    public ApiResponse<?> generateRefreshToken(String refreshToken){
//        try{
//            if (refreshToken == null || refreshToken.trim().isEmpty()) {
//                return ResponseUtil.getValidationErrorResponse("Refresh token is missing or invalid.");
//            }
//            log.info("Before passing the refresh token for the extraction of username");
//            if (!jwtService.validateRefreshToken(refreshToken)) {
//                return ResponseUtil.getValidationErrorResponse("Invalid or expired access token.");
//            }
//            log.info("Validation of refresh token is successful.");
//            String username=jwtService.extractUsername(refreshToken);
//            log.info("After passing the refresh token for the extraction of username"+username);
//            List<String> roles =jwtService.extractRolesFromRefreshToken(refreshToken);
//            authTokenRepository.setUsedAsTrue(refreshToken);
//            log.info("Used column value is changed.");
//            String accessToken = jwtService.generateToken(username,roles);
//            String refreshTokenRefresh = jwtService.generateRefreshToken(refreshToken);
//            UserInfo userDetails =authTokenRepository.getUserDetailsByRefreshToken(refreshTokenRefresh);
//            AuthToken authToken = AuthToken.builder()
//                    .accessToken(accessToken)
//                    .refreshToken(refreshToken)
//                    .issuedDate(LocalDateTime.now())
//                    .used(false)
//                    .authUser((UserInfo)userDetails)
//                    .build();
//            authTokenRepository.save(authToken);
//
//            log.info("After the repository saved of the authToken.");
//
//            AuthResponseDTO authResponseDTO =AuthResponseDTO.
//                    builder()
//                    .accessToken(accessToken)
//                    .refreshToken(refreshTokenRefresh)
//                    .build();
//
//            return ResponseUtil.getSuccessResponse(authResponseDTO,"Successfully Refreshed.");
//        }catch (Exception e) {
//            return ResponseUtil.getErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

//    @Override
//    public ApiResponse<?> getAllRefreshTokens(){
//       List<AuthToken> authTokens= authTokenRepository.findAll();
//
//       List<AuthResponseDTO> authResponses =new ArrayList<>();;
//       for (AuthToken authToken : authTokens) {
//           AuthResponseDTO authResponseDTO = new AuthResponseDTO();
//           authResponseDTO.setAccessToken(authToken.getAccessToken());
//           authResponseDTO.setRefreshToken(authToken.getRefreshToken());
//           authResponses.add(authResponseDTO);
//       }
//        return ResponseUtil.getSuccessResponse(authResponses, "Successfully retrieved all refresh tokens.");
//    }
}

package com.cosmotechintl.AttendanceSystem.Controller;



import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.LoginRequestDTO;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.ApiResponse;
import com.cosmotechintl.AttendanceSystem.service.AuthService;
import com.cosmotechintl.AttendanceSystem.utility.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    AuthService authService;

  @PostMapping("/login")
  public ApiResponse<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
    return authService.loginUser(loginRequestDTO);
  }

  @PostMapping("/refreshToken")
    public ApiResponse<?> refreshToken(@RequestHeader("Authorization") String authHeader) {

    String refreshToken = null;
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      refreshToken = authHeader.substring(7);  // Remove "Bearer " from the start of the string
    } else {
      return ResponseUtil.getValidationErrorResponse("Refresh token is missing or invalid.");
    }
      log.info("refreshToken: " + refreshToken);
      return authService.generateRefreshToken(refreshToken);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/getAllRefreshToken")
    public ApiResponse<?> getAllRefreshToken(){
      return authService.getAllRefreshTokens();
    }

}



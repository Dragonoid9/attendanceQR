package com.cosmotechintl.AttendanceSystem.Controller;



import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.LoginRequestDTO;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.ApiResponse;
import com.cosmotechintl.AttendanceSystem.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

//  @PostMapping("/refreshToken")
//    public ApiResponse<?> refreshToken(@RequestBody String refreshToken) {
//      log.info("refreshToken: " + refreshToken);
//      return authService.generateRefreshToken(refreshToken);
//  }
//
//  @PreAuthorize("ADMIN")
//  @GetMapping("/getallRefreshToken")
//    public ApiResponse<?> getAllRefreshToken(){
//      return authService.getAllRefreshTokens();
//    }

}



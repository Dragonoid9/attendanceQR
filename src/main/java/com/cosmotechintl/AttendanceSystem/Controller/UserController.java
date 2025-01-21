package com.cosmotechintl.AttendanceSystem.Controller;


import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.PasswordChangeDTO;
import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.RoleRequestDTO;
import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.UserPasswordResetDTO;
import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.UserRequestDTO;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.ApiResponse;
import com.cosmotechintl.AttendanceSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authUser")
public class UserController {

    @Autowired
    UserService userService;

    @PreAuthorize("hasRole('SUPER ADMIN')")
    @PostMapping("/addUser")
    public ApiResponse<?> saveUser(@RequestBody UserRequestDTO userRequestDTO) {
        return userService.saveUser(userRequestDTO);
    }

    @PreAuthorize("hasRole('SUPER ADMIN')")
    @PostMapping("/addRole")
    public ApiResponse<?> addRole(@RequestBody RoleRequestDTO roleRequestDTO) {
        return userService.addRole(roleRequestDTO);
    }

    @PreAuthorize("hasRole('SUPER ADMIN')")
    @PostMapping("/resetPassword")
    public ApiResponse<?> resetPassword(@RequestBody UserPasswordResetDTO userPasswordResetDTO) {
        return userService.resetPassword(userPasswordResetDTO);
    }

    @PreAuthorize("hasAnyRole('Employee', 'Intern')")
    @PostMapping("/changePassword")
    public ApiResponse<?> changePassword(@RequestBody PasswordChangeDTO passwordChangeDTO) {
        return userService.changePassword(passwordChangeDTO);
    }
}


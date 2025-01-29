package com.cosmotechintl.AttendanceSystem.Controller;


import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.*;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.ApiResponse;
import com.cosmotechintl.AttendanceSystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    UserService userService;

    @PreAuthorize("hasRole('SUPER ADMIN')")
    @PostMapping("/addUser")
    public ApiResponse<?> saveUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        return userService.saveUser(userRequestDTO);
    }

    @PreAuthorize("hasRole('SUPER ADMIN')")
    @PostMapping("/addRole")
    public ApiResponse<?> addRole(@Valid @RequestBody RoleRequestDTO roleRequestDTO) {
        return userService.addRole(roleRequestDTO);
    }

    @PreAuthorize("hasRole('SUPER ADMIN')")
    @PostMapping("/resetPassword")
    public ApiResponse<?> resetPassword(@Valid @RequestBody UserPasswordResetDTO userPasswordResetDTO) {
        return userService.resetPassword(userPasswordResetDTO);
    }

    @PreAuthorize("hasAnyRole('Employee', 'Intern')")
    @PostMapping("/changePassword")
    public ApiResponse<?> changePassword(@Valid @RequestBody PasswordChangeDTO passwordChangeDTO) {
        return userService.changePassword(passwordChangeDTO);
    }

    @PreAuthorize("hasAnyRole('Employee', 'Intern')")
    @PostMapping("/profile-pic")
    public ApiResponse<?> UpdateProfilePicture(@RequestParam("username") String username,
                                               @RequestParam("file") MultipartFile file) {
        return userService.updateProfilePicture(username,file);
    }
}


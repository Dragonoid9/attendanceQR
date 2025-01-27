package com.cosmotechintl.AttendanceSystem.service;

import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.*;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    ApiResponse<?> saveUser(UserRequestDTO userRequestDTO);

    ApiResponse<?> addRole(RoleRequestDTO roleRequestDTO);

    ApiResponse<?> resetPassword(UserPasswordResetDTO userPasswordResetDTO);

    ApiResponse<?> changePassword(PasswordChangeDTO passwordChangeDTO);

    ApiResponse<?> updateProfilePicture(ProfilePicRequestDTO profilePicRequestDTO);
}

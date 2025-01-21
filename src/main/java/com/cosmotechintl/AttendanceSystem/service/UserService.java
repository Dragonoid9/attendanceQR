package com.cosmotechintl.AttendanceSystem.service;

import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.PasswordChangeDTO;
import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.RoleRequestDTO;
import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.UserPasswordResetDTO;
import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.UserRequestDTO;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.ApiResponse;

public interface UserService {

    ApiResponse<?> saveUser(UserRequestDTO userRequestDTO);

    ApiResponse<?> addRole(RoleRequestDTO roleRequestDTO);

    ApiResponse<?> resetPassword(UserPasswordResetDTO userPasswordResetDTO);

    ApiResponse<?> changePassword(PasswordChangeDTO passwordChangeDTO);
}

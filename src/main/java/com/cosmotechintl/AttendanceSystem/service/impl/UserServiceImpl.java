package com.cosmotechintl.AttendanceSystem.service.impl;


import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.PasswordChangeDTO;
import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.RoleRequestDTO;
import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.UserPasswordResetDTO;
import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.UserRequestDTO;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.ApiResponse;
import com.cosmotechintl.AttendanceSystem.entity.UserInfo;
import com.cosmotechintl.AttendanceSystem.entity.UserRole;
import com.cosmotechintl.AttendanceSystem.exception.ResourceAlreadyExistsException;
import com.cosmotechintl.AttendanceSystem.exception.ResourceNotFoundException;
import com.cosmotechintl.AttendanceSystem.repository.UserInfoRepository;
import com.cosmotechintl.AttendanceSystem.repository.UserRoleRepository;
import com.cosmotechintl.AttendanceSystem.service.UserService;
import com.cosmotechintl.AttendanceSystem.utility.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    final PasswordEncoder passwordEncoder;
    final UserInfoRepository userRepository;
    final UserRoleRepository roleRepository;

    @Override
    public ApiResponse<?> saveUser(UserRequestDTO userRequestDTO) {

        String username = userRequestDTO.getUsername();
        String email = userRequestDTO.getEmail();

        if (userRepository.existsByUsername(username)) {
            throw new ResourceAlreadyExistsException("Username '" + username + "' is already taken.");
        }

        if (userRepository.existsByEmail(email)) {
            throw new ResourceAlreadyExistsException("Email '" + email + "' is already registered.");
        }

        String encodedPassword = passwordEncoder.encode(userRequestDTO.getPassword());
        List<UserRole> roles = userRequestDTO.getRoles().stream().map(roleName -> roleRepository.findByName(roleName).orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName))).toList();
        String phoneNumber = userRequestDTO.getPhoneNumber();
        String address = userRequestDTO.getAddress();
        double salary = userRequestDTO.getSalary();
        String department = userRequestDTO.getDepartment();
        LocalDate dateOfBirth = userRequestDTO.getDateOfBirth();
        LocalDate hireDate = userRequestDTO.getHireDate();
        String status = userRequestDTO.getStatus();

        UserInfo user = UserInfo.builder()
                .username(username)
                .email(email)
                .password(encodedPassword)
                .roles(roles)
                .address(address)
                .dateOfBirth(dateOfBirth)
                .hireDate(hireDate)
                .department(department)
                .phoneNumber(phoneNumber)
                .salary(salary)
                .status(status)
                .build();
        userRepository.save(user);
        return ResponseUtil.getSuccessResponse("User Saved Successfully");
    }

    @Override
    public ApiResponse<?> addRole(RoleRequestDTO roleRequestDTO) {
        String name = roleRequestDTO.getName();

        if (roleRepository.existsByName(name)) {
            throw new ResourceAlreadyExistsException("Role name: '" + name + "' is already registered.");
        }
        UserRole role = UserRole.builder().name(name).build();
        UserRole savedRole = roleRepository.save(role);

        return ResponseUtil.getSuccessResponse("Role Saved Successfully");
    }

    @Override
    public ApiResponse<?> resetPassword(UserPasswordResetDTO userPasswordResetDTO) {
        String username = userPasswordResetDTO.getUsername();
        String password = userPasswordResetDTO.getPassword();

        try {
            UserInfo userInfo = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

            String encodedPassword = passwordEncoder.encode(password);
            userInfo.setPassword(encodedPassword);
            userRepository.save(userInfo);

            return ResponseUtil.getSuccessResponse("Password Reset Successfully");
        } catch (Exception e) {
            return ResponseUtil.getErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ApiResponse<?> changePassword(PasswordChangeDTO passwordChangeDTO) {

        String password = passwordChangeDTO.getCurrentPassword();
        String newPassword = passwordChangeDTO.getNewPassword();
        String username = passwordChangeDTO.getUsername();
        try {
            UserInfo userInfo = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
            if (!passwordEncoder.matches(password, userInfo.getPassword())) {
                return ResponseUtil.getFailureResponse("Current password does not match", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            String encodedPassword = passwordEncoder.encode(newPassword);
            userInfo.setPassword(encodedPassword);
            userRepository.save(userInfo);

            return ResponseUtil.getSuccessResponse("Password Changed Successfully");
        } catch (Exception e) {
            return ResponseUtil.getErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

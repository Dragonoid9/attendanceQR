package com.cosmotechintl.AttendanceSystem.service;



import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.RoleRequestDTO;
import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.UserRequestDTO;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.ApiResponse;
import com.cosmotechintl.AttendanceSystem.entity.UserInfo;
import com.cosmotechintl.AttendanceSystem.entity.UserRole;
import com.cosmotechintl.AttendanceSystem.exception.ResourceAlreadyExistsException;
import com.cosmotechintl.AttendanceSystem.exception.ResourceNotFoundException;
import com.cosmotechintl.AttendanceSystem.repository.UserInfoRepository;
import com.cosmotechintl.AttendanceSystem.repository.UserRoleRepository;
import com.cosmotechintl.AttendanceSystem.utility.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserInfoRepository userRepository;
    private final UserRoleRepository roleRepository;

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
        List<UserRole> roles = userRequestDTO.getRoles().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(()-> new ResourceNotFoundException("Role not found: "+ roleName)))
                .toList();

        UserInfo user = UserInfo.builder()
                .username(username)
                .email(email)
                .password(encodedPassword)
                .roles(roles)
                .build();
        userRepository.save(user);
        return ResponseUtil.getSuccessResponse("User Saved Successfully");
    }

    @Override
    public ApiResponse<?> addRole(RoleRequestDTO roleRequestDTO) {
        String name = roleRequestDTO.getName();

        if(roleRepository.existsByName(name)){
            throw new ResourceAlreadyExistsException("Role name: '" + name + "' is already registered.");
        }
        UserRole role =UserRole.builder()
                .name(name)
                .build();
        UserRole savedRole = roleRepository.save(role);

        return ResponseUtil.getSuccessResponse("Role Saved Successfully");
    }

}

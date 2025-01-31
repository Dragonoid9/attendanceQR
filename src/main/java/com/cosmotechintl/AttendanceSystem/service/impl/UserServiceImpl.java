package com.cosmotechintl.AttendanceSystem.service.impl;


import com.cosmotechintl.AttendanceSystem.dto.RequestDTO.*;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.ApiResponse;
import com.cosmotechintl.AttendanceSystem.entity.UserInfo;
import com.cosmotechintl.AttendanceSystem.entity.UserRole;
import com.cosmotechintl.AttendanceSystem.exception.ResourceAlreadyExistsException;
import com.cosmotechintl.AttendanceSystem.exception.ResourceNotFoundException;
import com.cosmotechintl.AttendanceSystem.repository.UserInfoRepository;
import com.cosmotechintl.AttendanceSystem.repository.UserRoleRepository;
import com.cosmotechintl.AttendanceSystem.service.UserService;
import com.cosmotechintl.AttendanceSystem.utility.FileUtility;
import com.cosmotechintl.AttendanceSystem.utility.ImageProcessingUtil;
import com.cosmotechintl.AttendanceSystem.utility.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    final PasswordEncoder passwordEncoder;
    final UserInfoRepository userRepository;
    final UserRoleRepository roleRepository;

    @Override
    public ApiResponse<?> saveUser(UserRequestDTO userRequestDTO) {

        String username = userRequestDTO.getUsername();
        String email = userRequestDTO.getEmail();
        MultipartFile profilePic = userRequestDTO.getMultipartFile();

        if (userRepository.existsByUsername(username)) {
            throw new ResourceAlreadyExistsException("Username '" + username + "' is already taken.");
        }

        if (userRepository.existsByEmail(email)) {
            throw new ResourceAlreadyExistsException("Email '" + email + "' is already registered.");
        }

        String profilePicPath = null;
        if(!profilePic.isEmpty()){
            try {
                String originalFileName = profilePic.getOriginalFilename();
                String fileExtension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase() : "png";

                // Prepare the file paths
                String uploadDir = "storage/ProfilePics";
                String fileName = username + "_profilePic." + fileExtension;

                // Create a temporary file to save the uploaded image
                File tempFile = new File(uploadDir, fileName);
                profilePic.transferTo(tempFile);

                // Prepare the compressed image file path (same extension)
                File compressedFile = new File(uploadDir, username + "_profilePic_compressed." + fileExtension);
                ImageProcessingUtil.compressImage(tempFile, compressedFile, 0.7f);

                profilePicPath= FileUtility.saveFile(compressedFile,uploadDir,true);
                tempFile.delete();
            }catch (IOException e){
                return ResponseUtil.getErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
            }
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
                .profilePicture(profilePicPath)
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

    @Override
    public ApiResponse<?> updateProfilePicture(String username, MultipartFile profilePic) {

        File tempFile = null;
        try {
            if (profilePic.isEmpty()) {
                return ResponseUtil.getFailureResponse("No picture selected.", HttpStatus.BAD_REQUEST);
            }

            // Fetch user information from the database
            UserInfo userInfo = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

            // Get the original file extension from the uploaded image
            String originalFileName = profilePic.getOriginalFilename();
            String fileExtension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase() : "png";

            // Define the upload directory
            String uploadDir = "storage/ProfilePics";
            String fileName = username + "_profilePic." + fileExtension;

            log.info("Creating profile picture: " + fileName);
            // Create a temporary file to save the uploaded image
            Path tempDir = Files.createTempDirectory("imageTemp");
            tempFile = tempDir.resolve(profilePic.getOriginalFilename()).toFile();
            profilePic.transferTo(tempFile);

            log.info("Profile pic Transfered to tempFile: " + tempFile.getAbsolutePath());
            // Prepare the compressed image file path (same extension)
            File compressedFile = new File(uploadDir, username + "_profilePic_compressed." + fileExtension);
            log.info("Compressing profile picture: " + compressedFile.getAbsolutePath());
            // Compress the image based on its format
            ImageProcessingUtil.compressImage(tempFile, compressedFile, 0.7f);
            log.info("Profile pic Compressed to tempFile: " + compressedFile.getAbsolutePath());
            // Save the compressed image using FileUtility
            String profilePicPath = FileUtility.saveFile(compressedFile, uploadDir,true);
            log.info("Profile pic Path: " + profilePicPath);
            // Update the user's profile picture path
            userInfo.setProfilePicture(profilePicPath);
            userRepository.save(userInfo);
            log.info("After saving to database and before deleting the tempfile");

            log.info("After deleting the tempfile");
            return ResponseUtil.getSuccessResponse("Profile Picture Updated Successfully");
        } catch(Exception e){
            return ResponseUtil.getErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            // Ensure the temporary file is deleted even if an exception occurs
            if (tempFile != null && tempFile.exists()) {
                boolean deleted = tempFile.delete();
                log.info("Temporary file deleted: " + deleted);
            }
        }
    }
}

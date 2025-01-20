package com.cosmotechintl.AttendanceSystem.utility;


import com.cosmotechintl.AttendanceSystem.constant.ServerResponseCodeConstant;
import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.ApiResponse;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ResponseUtil {

    public static <T> ApiResponse<T> getResourceNotFoundResponse(String message) {
        return ApiResponse.<T>builder()
                .httpStatus(HttpStatus.OK)
                .message(message)
                .code(ServerResponseCodeConstant.NOT_FOUND)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> getResourceAlreadyExistsException(String message) {
        return ApiResponse.<T>builder()
                .httpStatus(HttpStatus.OK)
                .message(message)
                .code(ServerResponseCodeConstant.CONFLICT)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> getSuccessResponse(T data, String message) {
        return ApiResponse.<T>builder()
                .code(ServerResponseCodeConstant.OK)
                .message(message)
                .httpStatus(HttpStatus.OK)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> getSuccessResponse(String message) {
        return ApiResponse.<T>builder()
                .code(ServerResponseCodeConstant.OK)
                .message(message)
                .httpStatus(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ApiResponse<Object> getFailureResponse(String message, HttpStatus status) {
        return ApiResponse.builder()
                .code(ServerResponseCodeConstant.FAILURE)
                .message(message)
                .httpStatus(status)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ApiResponse<Object> getErrorResponse(Exception e, HttpStatus status) {
        return ApiResponse.builder()
                .code(ServerResponseCodeConstant.ERROR)
                .message(e.getMessage())
                .httpStatus(status)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ApiResponse<Object> getValidationErrorResponse(String validationMessage) {
        return ApiResponse.builder()
                .code(ServerResponseCodeConstant.VALIDATION_ERROR)
                .message(validationMessage)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> buildResponse(T data, int code, String message, HttpStatus status) {
        return ApiResponse.<T>builder()
                .data(data)
                .code(code)
                .message(message)
                .httpStatus(status)
                .timestamp(LocalDateTime.now())
                .build();
    }
//    Bhai tero aaukat xaina aaile
//    public static <T> ApiResponse<T> getAsyncResponse(T data, String message) {
//        return ApiResponse.<T>builder()
//                .code(ServerResponseCodeConstant.ASYNC)
//                .message(message)
//                .httpStatus(HttpStatus.ACCEPTED)
//                .data(data)
//                .isAsyncRequest(true)
//                .timestamp(LocalDateTime.now())
//                .build();
//    }
}
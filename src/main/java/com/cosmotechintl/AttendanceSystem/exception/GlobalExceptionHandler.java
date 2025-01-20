package com.cosmotechintl.AttendanceSystem.exception;


import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.ApiResponse;
import com.cosmotechintl.AttendanceSystem.utility.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ApiResponse<?> handlerResourceNotFoundException(ResourceNotFoundException e) {
        String message = e.getMessage();
        return ResponseUtil.getResourceNotFoundResponse(message);
    }

    @ExceptionHandler(value = ResourceAlreadyExistsException.class)
    public ApiResponse<?> handlerResourceAlreadyExistsException(ResourceAlreadyExistsException e) {
        String message = e.getMessage();
        return ResponseUtil.getResourceAlreadyExistsException(message);
    }

    @ExceptionHandler(value = TokenValidationException.class)
    public ApiResponse<?> handlerTokenValidationException(TokenValidationException e) {
        String message = e.getMessage();
        return ResponseUtil.getValidationErrorResponse(message);
    }

    @ExceptionHandler(value = TokenNotFoundException.class)
    public ApiResponse<?> handlerTokenNotFoundException(TokenNotFoundException e) {
        String message = e.getMessage();
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return ResponseUtil.getFailureResponse(message, status);
    }
}

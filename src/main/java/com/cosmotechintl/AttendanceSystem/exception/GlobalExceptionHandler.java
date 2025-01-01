package com.cosmotechintl.AttendanceSystem.exception;




import com.cosmotechintl.AttendanceSystem.dto.ResponseDTO.ApiResponse;
import com.cosmotechintl.AttendanceSystem.utility.ResponseUtil;
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
}

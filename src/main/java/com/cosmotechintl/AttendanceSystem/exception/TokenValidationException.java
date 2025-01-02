package com.cosmotechintl.AttendanceSystem.exception;

public class TokenValidationException extends RuntimeException {
    public TokenValidationException(){
        super("TokenValidationException");
    }
    public TokenValidationException(String message) {
        super(message);
    }
}

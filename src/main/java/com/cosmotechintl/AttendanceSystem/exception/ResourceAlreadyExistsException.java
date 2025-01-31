package com.cosmotechintl.AttendanceSystem.exception;

public class ResourceAlreadyExistsException extends RuntimeException {

    public ResourceAlreadyExistsException() {
        super("Resource already exists !!!");
    }

    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}

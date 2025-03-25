package com.mareen.userservice.exception;

public class RoleNotExistException extends RuntimeException {
    public RoleNotExistException(String message) {
        super(message);
    }
}

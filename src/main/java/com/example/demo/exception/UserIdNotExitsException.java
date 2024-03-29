package com.example.demo.exception;

public class UserIdNotExitsException extends RuntimeException{
    public UserIdNotExitsException(String message) {
        super(message);
    }
}

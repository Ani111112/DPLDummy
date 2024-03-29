package com.example.demo.exception;

public class UserIdAlreadyUserdException extends RuntimeException{
    public UserIdAlreadyUserdException(String message) {
        super(message);
    }
}

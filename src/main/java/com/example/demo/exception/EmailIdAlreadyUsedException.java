package com.example.demo.exception;

public class EmailIdAlreadyUsedException extends RuntimeException{
    public EmailIdAlreadyUsedException(String message) {
        super(message);
    }
}

package com.example.demo.exception;

public class MobileNumberAlreadyUsedException extends RuntimeException{
    public MobileNumberAlreadyUsedException(String message) {
        super(message);
    }
}

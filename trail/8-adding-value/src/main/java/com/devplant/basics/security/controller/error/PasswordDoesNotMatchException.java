package com.devplant.basics.security.controller.error;


public class PasswordDoesNotMatchException extends RuntimeException {

    public PasswordDoesNotMatchException(String message) {
        super(message);
    }
}

package com.devplant.training.exceptions;


public class PasswordDoesNotMatchException extends RuntimeException {

    public PasswordDoesNotMatchException(String s) {
        super(s);
    }

}

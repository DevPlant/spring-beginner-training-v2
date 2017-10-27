package com.devplant.training.account.exception;

public class PasswordsDoNotMatchException extends RuntimeException {

    public PasswordsDoNotMatchException(String message){
        super(message);
    }
}

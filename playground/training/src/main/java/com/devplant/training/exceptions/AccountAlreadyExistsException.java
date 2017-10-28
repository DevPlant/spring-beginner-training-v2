package com.devplant.training.exceptions;

public class AccountAlreadyExistsException extends RuntimeException {

    public AccountAlreadyExistsException(String message){
        super(message);
    }

}

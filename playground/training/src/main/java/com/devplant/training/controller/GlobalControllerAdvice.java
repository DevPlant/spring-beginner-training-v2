package com.devplant.training.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.devplant.training.exceptions.AccountAlreadyExistsException;
import com.devplant.training.exceptions.ErrorMessage;
import com.devplant.training.exceptions.ObjectNotFoundException;
import com.devplant.training.exceptions.PasswordDoesNotMatchException;


@RestControllerAdvice
public class GlobalControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {ObjectNotFoundException.class})
    public ErrorMessage handleNotFoundException(
            ObjectNotFoundException e) {
        return new ErrorMessage(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {AccountAlreadyExistsException.class})
    public ErrorMessage handleAccountAlreadyExistsException(
            AccountAlreadyExistsException e) {
        return new ErrorMessage(e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(value = {PasswordDoesNotMatchException.class})
    public ErrorMessage handlePasswordDoesNotMatchException(
            PasswordDoesNotMatchException e) {
        return new ErrorMessage(e.getMessage());
    }

}

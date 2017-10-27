package com.devplant.training.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.devplant.training.exceptions.ErrorMessage;
import com.devplant.training.exceptions.ObjectNotFoundException;


@RestControllerAdvice
public class GlobalControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {ObjectNotFoundException.class})
    public ErrorMessage handleNotFoundException(
            ObjectNotFoundException e) {
        return new ErrorMessage(e.getMessage());
    }

}

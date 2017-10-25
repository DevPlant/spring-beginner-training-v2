package com.devplant.basics.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.devplant.basics.security.controller.error.InvalidRequestException;
import com.devplant.basics.security.controller.error.NotFoundException;
import com.devplant.basics.security.controller.model.ErrorResponse;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = NotFoundException.class)
    @ResponseBody
    public ErrorResponse handleNotFoundException(NotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = InvalidRequestException.class)
    @ResponseBody
    public ErrorResponse handleInvalidRequestException(InvalidRequestException e) {
        return new ErrorResponse(e.getMessage());
    }

}

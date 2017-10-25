package com.devplant.snippet.rest;

public class CustomRestException extends RuntimeException {

    public CustomRestException(String message) {
        super(message);
    }
}

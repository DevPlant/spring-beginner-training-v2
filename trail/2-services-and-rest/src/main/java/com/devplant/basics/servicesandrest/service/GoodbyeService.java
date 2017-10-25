package com.devplant.basics.servicesandrest.service;

public class GoodbyeService {

    private String message;

    public GoodbyeService(String message) {
        this.message = message;
    }

    public String sayGoodbye() {
        return message;
    }
}
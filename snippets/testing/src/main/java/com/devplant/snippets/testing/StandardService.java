package com.devplant.snippets.testing;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StandardService {

    @Autowired
    private SubService subService;

    public String echoPretty(String message) {
        return "Echo: " + subService.makeItPretty(message);
    }


    @Service
    public static class SubService {

        public String makeItPretty(String message) {

            return message.toUpperCase() + " Uppercase Sparkle !";
        }
    }
}

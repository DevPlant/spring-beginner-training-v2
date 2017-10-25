package com.devplant.snippets.beans;

import org.springframework.stereotype.Service;

@Service
public class CoffeeBean {

    public String getCoffee() {
        return "This is magic Coffee";
    }

}

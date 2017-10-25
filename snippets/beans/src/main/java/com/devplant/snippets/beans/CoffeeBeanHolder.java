package com.devplant.snippets.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;

@Service
public class CoffeeBeanHolder {

    @Autowired
    @Getter
    private CoffeeBean coffeeBean;

}

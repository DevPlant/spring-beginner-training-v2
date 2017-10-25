package com.devplant.snippets.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AllBeansAreSingletons implements CommandLineRunner {

    @Autowired
    private CoffeeBean coffeeBean;

    @Autowired
    private CoffeeBeanHolder coffeeBeanHolder;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void run(String... strings) throws Exception {
        CoffeeBean contextCoffeeBean = applicationContext.getBean(CoffeeBean.class);

        if (coffeeBean == contextCoffeeBean) {
            log.info(" ----> We are all equal!");
        }

        CoffeeBean fromHolder = coffeeBeanHolder.getCoffeeBean();

        if (coffeeBean == fromHolder) {
            log.info(" ----> See, we're all the same reference! and the same object!");
        }
    }
}

package com.devplant.basics.servicesandrest.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devplant.basics.servicesandrest.properties.SimpleProperties;
import com.devplant.basics.servicesandrest.service.GoodbyeService;

@Configuration
@EnableConfigurationProperties(SimpleProperties.class)
public class SimpleConfiguration {

    @Autowired
    private SimpleProperties simpleProperties;

    @Bean
    public GoodbyeService goodbyeService() {
        return new GoodbyeService(simpleProperties.getGoodbyeMessage());
    }


}
package com.devplant.snippets.properties;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@SpringBootApplication
@EnableConfigurationProperties(CustomProperties.class)
public class PropertiesApplication {

    @Value("${direct.property:defaultValueGoesHere}")
    private String directProperty;

    @Autowired
    private CustomProperties customProperties;

    @PostConstruct
    protected void postConstruct() {

        log.info(" ---> Showing Current Properties: " + customProperties.toString());
        log.info(" ---> Direct Property: "+directProperty);
    }

    public static void main(String[] args) {
        SpringApplication.run(PropertiesApplication.class, args);
    }
}

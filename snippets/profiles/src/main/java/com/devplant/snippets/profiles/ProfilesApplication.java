package com.devplant.snippets.profiles;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties(ProfileProperties.class)
public class ProfilesApplication {

    @Autowired
    private ProfileProperties profileProperties;

    @Autowired
    private ProfileBasedService profileBasedService;


    @PostConstruct
    protected void postConstruct() {
        log.info("Properties are: " + profileProperties);

        profileBasedService.implementationBasedOnProfile();
    }


    public static void main(String[] args) {
        SpringApplication.run(ProfilesApplication.class, args);
    }
}

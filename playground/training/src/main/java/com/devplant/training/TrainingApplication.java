package com.devplant.training;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

import com.devplant.training.proprties.InitialAccountProperties;


@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties(InitialAccountProperties.class)
public class TrainingApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrainingApplication.class, args);
    }

}

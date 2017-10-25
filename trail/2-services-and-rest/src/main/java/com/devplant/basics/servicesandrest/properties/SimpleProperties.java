package com.devplant.basics.servicesandrest.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties("grettings")
public class SimpleProperties {

    private String goodbyeMessage;

}
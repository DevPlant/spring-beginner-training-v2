package com.devplant.snippets.profiles;


import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties("app.profile")
public class ProfileProperties {

    private String message;

    private String alternativeMessage;
}

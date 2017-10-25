package com.devplant.basics.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties("library.data")
public class LibraryProperties {

    private String bookDataFile;
    private String authorDataFile;

}

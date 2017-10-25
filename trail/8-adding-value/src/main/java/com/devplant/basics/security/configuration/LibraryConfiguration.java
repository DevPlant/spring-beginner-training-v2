package com.devplant.basics.security.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.devplant.basics.security.properties.AdminUserProperties;
import com.devplant.basics.security.properties.LibraryProperties;

import it.ozimov.springboot.mail.configuration.EnableEmailTools;

@Configuration
@EnableEmailTools
@EnableConfigurationProperties({AdminUserProperties.class, LibraryProperties.class})
public class LibraryConfiguration {

}

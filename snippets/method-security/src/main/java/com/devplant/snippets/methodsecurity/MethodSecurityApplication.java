package com.devplant.snippets.methodsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(MethodSecurityApplication.class, args);
    }
}

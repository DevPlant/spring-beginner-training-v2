package com.devplant.basics.security.properties;


import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.devplant.basics.security.model.User;

import lombok.Data;

@Data
@ConfigurationProperties("library.admin")
public class AdminUserProperties {

    private List<User> adminUsers = new ArrayList<>();
}

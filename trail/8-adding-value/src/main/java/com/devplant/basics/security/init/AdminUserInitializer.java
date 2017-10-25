package com.devplant.basics.security.init;


import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.devplant.basics.security.properties.AdminUserProperties;
import com.devplant.basics.security.repository.UserRepository;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Profile("admin-users")
public class AdminUserInitializer implements CommandLineRunner {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminUserProperties adminUserProperties;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... strings) throws Exception {

        userRepository.save(adminUserProperties.getAdminUsers().stream().map(
                user -> {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    user.setRoles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"));
                    user.setEnabled(true);
                    user.setReservedBooks(Lists.newArrayList());

                    log.info("Created Admin User: {}", user.getUsername());
                    return user;
                }
        ).collect(Collectors.toList()));

    }

}

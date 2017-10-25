package com.devplant.basics.security;

import java.util.Arrays;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.devplant.basics.security.model.User;
import com.devplant.basics.security.repository.UserRepository;

public abstract class AbstractFlowTest {

    @LocalServerPort
    private int port;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Before
    public void before() {

        User admin = User.builder().username("admin").password(passwordEncoder.encode("admin")).roles(Arrays.asList("ROLE_USER", "ROLE_ADMIN")).enabled(true).build();
        User user = User.builder().username("user").password(passwordEncoder.encode("user")).roles(Arrays.asList("ROLE_USER")).enabled(true).build();

        userRepository.save(Arrays.asList(admin, user));
    }


    public String createTestUrl(String uri) {
        return "http://localhost:" + port + uri;
    }

}

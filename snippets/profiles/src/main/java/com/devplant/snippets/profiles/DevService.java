package com.devplant.snippets.profiles;


import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Profile("dev")
public class DevService implements ProfileBasedService {

    @Override
    public void implementationBasedOnProfile() {
        log.info(" --->  Greetings from Dev Implementation");
    }

}

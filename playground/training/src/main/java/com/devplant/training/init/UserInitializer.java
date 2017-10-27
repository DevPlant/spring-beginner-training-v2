package com.devplant.training.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.devplant.training.proprties.InitialAccountProperties;
import com.devplant.training.repo.UserRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserInitializer implements CommandLineRunner {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private InitialAccountProperties initialAccountProperties;

    @Override
    public void run(String... strings) throws Exception {
        //userRepo.save(initialAccountProperties.getAccounts());

        initialAccountProperties.getAccounts().forEach(
                account -> {
                    account.setEnabled(true);
                    account.setPassword(passwordEncoder.encode(
                            account.getPassword()
                    ));
                    log.info("Creating account: {}", account);
                    userRepo.save(account);
                }
        );
    }

}

package com.devplant.training.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.devplant.training.proprties.InitialAccountProperties;
import com.devplant.training.repo.AccountRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AccountInitializer implements CommandLineRunner {

    @Autowired
    private AccountRepo accountRepo;

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
                    log.info("Creating service: {}", account);
                    accountRepo.save(account);
                }
        );
    }

}

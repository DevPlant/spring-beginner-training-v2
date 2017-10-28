package com.devplant.training.service;

import java.util.Arrays;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devplant.training.entity.Account;
import com.devplant.training.exceptions.AccountAlreadyExistsException;
import com.devplant.training.exceptions.PasswordDoesNotMatchException;
import com.devplant.training.model.AccountRegistrationRequest;
import com.devplant.training.model.ChangePasswordRequest;
import com.devplant.training.repo.AccountRepo;

@Service
public class AccountService {

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Account me() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        return accountRepo.findOne(authentication.getName());
    }

    public void registerAccount(@Valid AccountRegistrationRequest request) {
        if (accountRepo.exists(request.getUsername())) {
            throw new AccountAlreadyExistsException(
                    "Account: " + request.getUsername() + " already registered!");
        }

        Account account = Account.builder()
                .enabled(true)
                .roles(Arrays.asList("ROLE_USER"))
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        accountRepo.save(account);
    }

    @Transactional
    public void changePassword(@Valid ChangePasswordRequest request) {
        Account account = me();
        if (!passwordEncoder.matches(request.getOldPassword(),
                account.getPassword())) {
            throw new PasswordDoesNotMatchException(
                    "Old and current password do not match"
            );
        }

        account.setPassword(passwordEncoder.encode(
                request.getNewPassword()
        ));

    }

}

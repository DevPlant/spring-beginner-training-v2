package com.devplant.training.account.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devplant.training.account.exception.AccountAlreadyExistsException;
import com.devplant.training.account.exception.PasswordsDoNotMatchException;
import com.devplant.training.account.model.ChangePasswordRequest;
import com.devplant.training.account.model.PublicAccountResponse;
import com.devplant.training.account.model.RegisterAccountRequest;
import com.devplant.training.entity.Account;
import com.devplant.training.repo.AccountRepo;

@Service
public class AccountService {

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public PublicAccountResponse getMyself() {
        Account account = accountRepo.findOne(SecurityContextHolder.getContext().getAuthentication().getName());
        return PublicAccountResponse.builder()
                .username(account.getUsername())
                .roles(account.getRoles())
                .build();
    }

    public void createAccount(RegisterAccountRequest registerAccountRequest) {
        if (accountRepo.exists(registerAccountRequest.getUsername())) {
            throw new AccountAlreadyExistsException(
                    "Account: " + registerAccountRequest.getUsername()
                            + " already exists");
        } else {
            Account account = Account.builder()
                    .enabled(true)
                    .username(registerAccountRequest.getUsername())
                    .password(passwordEncoder.encode(registerAccountRequest.getPassword()))
                    .roles(Collections.singletonList("ROLE_USER")).build();
            accountRepo.save(account);
        }
    }

    @Transactional
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        Account account = accountRepo.findOne(SecurityContextHolder.getContext().getAuthentication().getName());
        if (passwordEncoder.matches(changePasswordRequest.getOldPassword(), account.getPassword())) {
            account.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        } else {
            throw new PasswordsDoNotMatchException("Your old password does not match");
        }
    }
}

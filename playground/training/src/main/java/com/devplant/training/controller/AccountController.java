package com.devplant.training.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devplant.training.entity.Account;
import com.devplant.training.model.AccountRegistrationRequest;
import com.devplant.training.model.ChangePasswordRequest;
import com.devplant.training.service.AccountService;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/me")
    public Account me(){
        return accountService.me();
    }

    @PostMapping("/register")
    public void register(@RequestBody  AccountRegistrationRequest request){
        accountService.registerAccount(request);
    }

    @PostMapping("/change-password")
    public void changePassword(@RequestBody  ChangePasswordRequest request){
        accountService.changePassword(request);
    }

}

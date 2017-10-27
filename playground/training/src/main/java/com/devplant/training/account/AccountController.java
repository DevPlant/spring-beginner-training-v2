package com.devplant.training.account;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.devplant.training.account.exception.AccountAlreadyExistsException;
import com.devplant.training.account.exception.PasswordsDoNotMatchException;
import com.devplant.training.account.model.ChangePasswordRequest;
import com.devplant.training.account.model.PublicAccountResponse;
import com.devplant.training.account.model.RegisterAccountRequest;
import com.devplant.training.account.service.AccountService;
import com.devplant.training.exceptions.ErrorMessage;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/me")
    public PublicAccountResponse me() {
        return accountService.getMyself();
    }

    @PostMapping("/register")
    public void register(@RequestBody RegisterAccountRequest registerAccountRequest) {
        accountService.createAccount(registerAccountRequest);
    }

    @PostMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public void changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        accountService.changePassword(changePasswordRequest);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {AccountAlreadyExistsException.class})
    public ErrorMessage handleAccountAlreadyExistsException(
            AccountAlreadyExistsException e) {
        return new ErrorMessage(e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = {PasswordsDoNotMatchException.class})
    public ErrorMessage handlePasswordsDoNotMatchException(
            PasswordsDoNotMatchException e) {
        return new ErrorMessage(e.getMessage());
    }


}

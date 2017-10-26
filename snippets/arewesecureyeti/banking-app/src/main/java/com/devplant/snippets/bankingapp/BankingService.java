package com.devplant.snippets.bankingapp;


import java.security.Principal;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BankingService {

    public void loanMoney(long amount, Principal user) {
        log.info(" -----> Lending {}$ to our dear customer {}", amount, user.getName());
    }

    public void sendMoneyTo(BankingController.SendMoneyRequest sendMoneyRequest, Principal principal) {
        log.info(" -----> User {} is sending {}$ to account {}", principal.getName(), sendMoneyRequest.getAmount(), sendMoneyRequest.getIban());
    }
}

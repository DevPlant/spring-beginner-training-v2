package com.devplant.snippets.bankingapp;


import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/api/banking")
public class BankingController {

    @Autowired
    private BankingService bankingService;


    @GetMapping("/borrow")
    public String loanMoney(@RequestParam("amount") long amount, Principal principal) {
        bankingService.loanMoney(amount, principal);
        return "The bank has just loaned you: " + amount + "$ at 100% interest!";
    }

    // A little bit different for FORM Data, Don't use @RequestBody annotation
    @PostMapping(value = "/send-money", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String sendMoneyTo(SendMoneyRequest sendMoneyRequest, Principal principal) {
        bankingService.sendMoneyTo(sendMoneyRequest, principal);
        return "You've just sent " + sendMoneyRequest.getAmount() + "$ to " + sendMoneyRequest.getIban();
    }

    @Data
    public static class SendMoneyRequest {
        private int amount;
        private String iban;
    }
}

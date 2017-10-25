package com.devplant.basics.security.service.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.devplant.basics.security.service.EmailSendService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Profile("email-notifications")
public class EmailNotificationService implements INotificationService {

    @Autowired
    private EmailSendService emailSendService;

    @Override
    public void notifyUserAboutRegister(String username) {
        emailSendService.sendEmail(EmailSendService.EmailModel.builder()
                .subject("Your DevPlant Library Account has been created")
                .message("Welcome To our Library!")
                .username(username)
                .build());
    }

    @Override
    public void notifyUserAboutApproval(String bookName, String username, long stockId) {
        emailSendService.sendEmail(EmailSendService.EmailModel.builder()
                .subject("Your Book Reservation has been approved!")
                .message("Your reservation for " + bookName + " has been approved. Your pickupId is " + stockId)
                .username(username)
                .build());
    }

    @Override
    public void notifyUserAboutPickup(String bookName, String username, long stockId) {
        emailSendService.sendEmail(EmailSendService.EmailModel.builder()
                .subject("You just picked up: " + bookName)
                .message("You just picked up your reserved book from our library!")
                .username(username)
                .build());
    }

    @Override
    public void notifyUserAboutPickupExpired(String bookName, String username, long stockId) {
        emailSendService.sendEmail(EmailSendService.EmailModel.builder()
                .subject("Your pickup has expired!")
                .message("Your reservation for " + bookName + " has expired! You can no longer use the pickupId " + stockId)
                .username(username)
                .build());
    }

    @Override
    public void notifyUserAboutReturnDateExpired(String bookName, String username, long stockId) {
        emailSendService.sendEmail(EmailSendService.EmailModel.builder()
                .subject("Your need to return " + bookName)
                .message("Your return date for " + bookName + " has expired! Please return it ASAP!")
                .username(username)
                .build());
    }

    @Override
    public void notifyUserAboutCancel(String bookName, String username, long stockId) {
        emailSendService.sendEmail(EmailSendService.EmailModel.builder()
                .subject("Your Book Reservation has been canceled!")
                .message("Your reservation for " + bookName + " has been canceled, we are sorry!")
                .username(username)
                .build());
    }

    @Override
    public void notifyUserAboutBadBehaviour(String bookName, String username, long stockId) {
        emailSendService.sendEmail(EmailSendService.EmailModel.builder()
                .subject("Keep It!")
                .message("Your held on to " + bookName + " for a very long time, so we decided to let you keep it! P.S. don't come back!")
                .username(username)
                .build());
    }


}

package com.devplant.basics.security.service.notification;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Profile("logging-notifications")
public class LoggingNotificationService implements INotificationService {

    @Override
    public void notifyUserAboutRegister(String username) {
        log.info("Notifying {} regarding account creation");
    }

    @Override
    public void notifyUserAboutApproval(String bookName, String username, long stockId) {
        log.info("Notifying {} regarding approval and pickup-date of book {} with reservation {}", username, bookName, stockId);
    }

    @Override
    public void notifyUserAboutPickup(String bookName, String username, long stockId) {
        log.info("Notifying {} regarding pickup and return-date of book {} with reservation {}", username, bookName, stockId);
    }

    @Override
    public void notifyUserAboutPickupExpired(String bookName, String username, long stockId) {
        log.info("Notifying {} regarding expiration of pickup-date of book {} with reservation {}", username, bookName, stockId);
    }

    @Override
    public void notifyUserAboutReturnDateExpired(String bookName, String username, long stockId) {
        log.info("Notifying {} regarding expiration of return date of book {} with reservation {}", username, bookName, stockId);
    }

    @Override
    public void notifyUserAboutCancel(String bookName, String username, long stockId) {
        log.info("Notifying {} regarding cancel of book {} with reservation {}", username, bookName, stockId);
    }

    @Override
    public void notifyUserAboutBadBehaviour(String bookName, String username, long stockId) {
        log.info("Notifying {} regarding bad behaviour regarding book {} with reservation {}", username, bookName, stockId);
    }
}

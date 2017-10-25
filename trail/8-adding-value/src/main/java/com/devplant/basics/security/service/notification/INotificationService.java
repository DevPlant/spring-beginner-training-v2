package com.devplant.basics.security.service.notification;


import org.springframework.scheduling.annotation.Async;

public interface INotificationService {

    @Async
    void notifyUserAboutRegister(String username);

    @Async
    void notifyUserAboutApproval(String bookName, String username, long stockId);

    @Async
    void notifyUserAboutPickup(String bookName, String username, long stockId);

    @Async
    void notifyUserAboutPickupExpired(String bookName, String username, long stockId);

    @Async
    void notifyUserAboutReturnDateExpired(String bookName, String username, long stockId);

    @Async
    void notifyUserAboutCancel(String bookName, String username, long stockId);

    @Async
    void notifyUserAboutBadBehaviour(String bookName, String username, long stockId);
}

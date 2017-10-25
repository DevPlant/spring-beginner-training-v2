package com.devplant.basics.security;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.devplant.basics.security.service.notification.INotificationService;

import it.ozimov.springboot.mail.service.EmailService;
import it.ozimov.springboot.mail.service.exception.CannotSendEmailException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.profiles.active=email-notifications")
public class EmailNotificationServiceTest extends AbstractFlowTest {

    @Autowired
    private INotificationService emailNotificationService;

    @MockBean
    private EmailService emailService;

    private String username = "timo.bejan@gmail.com";

    private String book = "The Book";

    @Before
    public void before() {
        try {
            when(emailService.send(Mockito.any(), Mockito.any(), Mockito.any())).thenThrow(new EmailCalledException());
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Test(expected = EmailCalledException.class)
    public void testNotifyUserAboutReturnDateExpired() throws CannotSendEmailException, InterruptedException {

        emailNotificationService.notifyUserAboutReturnDateExpired(book, username, 1);
        verify(emailService.send(Mockito.any(), Mockito.any(), Mockito.any()));


    }

    @Test(expected = EmailCalledException.class)
    public void testNotifyUserAboutPickup() throws CannotSendEmailException {

        emailNotificationService.notifyUserAboutPickup(book, username, 1);
        verify(emailService.send(Mockito.any(), Mockito.any(), Mockito.any()));

    }

    @Test(expected = EmailCalledException.class)
    public void testNotifyUserAboutPickupExpired() throws CannotSendEmailException {
        emailNotificationService.notifyUserAboutPickupExpired(book, username, 1);
        verify(emailService.send(Mockito.any(), Mockito.any(), Mockito.any()));


    }

    @Test(expected = EmailCalledException.class)
    public void testNotifyUserAboutBadBehaviour() throws CannotSendEmailException {

        emailNotificationService.notifyUserAboutReturnDateExpired(book, username, 1);
        verify(emailService.send(Mockito.any(), Mockito.any(), Mockito.any()));

    }

    @Test(expected = EmailCalledException.class)
    public void testNotifyUserAboutCancel() throws CannotSendEmailException {

        emailNotificationService.notifyUserAboutCancel(book, username, 1);
        verify(emailService.send(Mockito.any(), Mockito.any(), Mockito.any()));

    }

    @Test(expected = EmailCalledException.class)
    public void testNotifyUserAboutRegister() throws CannotSendEmailException {

        emailNotificationService.notifyUserAboutRegister(username);
        verify(emailService.send(Mockito.any(), Mockito.any(), Mockito.any()));

    }

    @Test(expected = EmailCalledException.class)
    public void testNotifyUserAboutApproval() throws CannotSendEmailException {

        emailNotificationService.notifyUserAboutApproval(book, username, 1);
        verify(emailService.send(Mockito.any(), Mockito.any(), Mockito.any()));

    }

    public static class EmailCalledException extends RuntimeException {

    }

}

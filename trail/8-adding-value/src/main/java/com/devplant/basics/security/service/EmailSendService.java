package com.devplant.basics.security.service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.InternetAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import it.ozimov.springboot.mail.model.Email;
import it.ozimov.springboot.mail.model.defaultimpl.DefaultEmail;
import it.ozimov.springboot.mail.service.EmailService;
import it.ozimov.springboot.mail.service.exception.CannotSendEmailException;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailSendService {

    @Value("${spring.mail.username:devplant.spring@gmail.ro}")
    private String severEmailAddress;

    @Autowired
    private EmailService emailService;

    @Data
    @Builder
    public static class EmailModel {
        private String subject;
        private String username;
        private String message;
    }

    public void sendEmail(EmailModel emailModel) {
        try {
            Email email = DefaultEmail.builder().from(new InternetAddress(severEmailAddress, "DevPlant")).to(Lists
                    .newArrayList(new InternetAddress(emailModel.getUsername(), emailModel.getUsername())))
                    .subject("Activate your DevPlant Library Account").body("").encoding("UTF-8").build();

            final Map<String, Object> modelObject = new HashMap<>();
            modelObject.put("username", emailModel.getUsername());
            modelObject.put("message", emailModel.getMessage());

            emailService.send(email, "notify-user.ftl", modelObject);
        } catch (UnsupportedEncodingException e) {
            log.error("Something is miss-configured, as this cannot happen", e);
        } catch (CannotSendEmailException e) {
            log.error("Cloud not send activation-email", e);
        }

    }
}

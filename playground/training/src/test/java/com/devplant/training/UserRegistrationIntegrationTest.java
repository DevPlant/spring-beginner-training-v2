package com.devplant.training;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.test.context.junit4.SpringRunner;

import com.devplant.training.configuration.SecurityConfig;
import com.devplant.training.entity.Account;
import com.devplant.training.model.AccountRegistrationRequest;
import com.devplant.training.model.ChangePasswordRequest;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TrainingApplication.class, UserRegistrationIntegrationTest.TestSecurityConfig.class}
        , webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRegistrationIntegrationTest {

    @LocalServerPort
    private int localServerPort;

    @Order(99)
    @Configuration
    public static class TestSecurityConfig extends SecurityConfig {

        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception {
            httpSecurity.formLogin().disable();
            httpSecurity.csrf().disable();
            httpSecurity.httpBasic();

            configureAppSpecificHttpSecurityPaths(httpSecurity);
        }
    }


    @Test
    public void testRegistration() {
        TestRestTemplate testRestTemplate = new TestRestTemplate();

        // register as a user
        AccountRegistrationRequest request = new AccountRegistrationRequest();
        request.setUsername("supertimo");
        request.setPassword("password");
        ResponseEntity<Void> response = testRestTemplate.postForEntity(getServerBaseUrl() + "/account/register",
                request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        TestRestTemplate meRestTemplate = new TestRestTemplate("supertimo","password");

        ResponseEntity<Account> meAsAccount =
                meRestTemplate.getForEntity(getServerBaseUrl() + "/account/me", Account.class);

        assertThat(meAsAccount.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(meAsAccount.getBody().getRoles()).containsExactly("ROLE_USER");

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setNewPassword("newpassword");
        changePasswordRequest.setOldPassword("password");

        ResponseEntity<Void> changeResponse = meRestTemplate.postForEntity(
                getServerBaseUrl() + "/account/change-password",
                changePasswordRequest, Void.class);
        assertThat(changeResponse.getStatusCode()).isEqualTo(HttpStatus.OK);


        ResponseEntity<Account> meAsAccountFailed =
                meRestTemplate.getForEntity(getServerBaseUrl() + "/account/me", Account.class);
        assertThat(meAsAccountFailed.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);


        meRestTemplate = new TestRestTemplate("supertimo","newpassword");
        ResponseEntity<Account> meOk =
                meRestTemplate.getForEntity(getServerBaseUrl() + "/account/me", Account.class);
        assertThat(meOk.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private String getServerBaseUrl() {
        return "http://localhost:" + localServerPort;
    }

}

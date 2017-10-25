package com.devplant.basics.security;


import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.devplant.basics.security.controller.model.ChangePasswordRequest;
import com.devplant.basics.security.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.profiles.active=logging-notifications")
public class UserFlowTest extends AbstractFlowTest {

    @Test
    public void testUserRegisterFlow() {

        TestRestTemplate registerRestTemplate = new TestRestTemplate();

        User user = new User();
        user.setUsername("test-user");
        user.setPassword("test-password");


        ResponseEntity<Void> registeredUser = registerRestTemplate.postForEntity(createTestUrl("/api/user/register"), user, Void.class);
        assertThat(registeredUser.getStatusCode()).isEqualTo(HttpStatus.OK);


        TestRestTemplate loginRestTemplate = new TestRestTemplate("test-user", "test-password");
        ResponseEntity<User> currentPrincipalUser = loginRestTemplate.getForEntity(createTestUrl("/api/user/me"), User.class);
        assertThat(currentPrincipalUser.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(currentPrincipalUser.getBody().getRoles()).containsExactly("ROLE_USER");


        ChangePasswordRequest failRequest = new ChangePasswordRequest("test-password-error", "new-password");
        ResponseEntity<Void> failRequestResponse = loginRestTemplate.postForEntity(createTestUrl("/api/user/change-password"), failRequest, Void.class);
        assertThat(failRequestResponse.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

        ChangePasswordRequest successRequest = new ChangePasswordRequest("test-password", "new-password");
        ResponseEntity<Void> changePasswordResponse = loginRestTemplate.postForEntity(createTestUrl("/api/user/change-password"), successRequest, Void.class);
        assertThat(changePasswordResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<User> afterPasswordChangePrincipal = loginRestTemplate.getForEntity(createTestUrl("/api/user/me"), User.class);
        assertThat(afterPasswordChangePrincipal.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        loginRestTemplate = new TestRestTemplate("test-user", "new-password");

        ResponseEntity<User> afterPasswordChangeSuccessPrincipal = loginRestTemplate.getForEntity(createTestUrl("/api/user/me"), User.class);
        assertThat(afterPasswordChangeSuccessPrincipal.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Void> deleteAccountResult = loginRestTemplate.exchange(createTestUrl("/api/user/delete-my-account"), HttpMethod.DELETE, null, Void.class);
        assertThat(deleteAccountResult.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<User> afterDelete = loginRestTemplate.getForEntity(createTestUrl("/api/user/me"), User.class);
        assertThat(afterDelete.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

    }

}
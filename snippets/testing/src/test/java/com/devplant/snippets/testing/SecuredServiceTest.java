package com.devplant.snippets.testing;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestingApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecuredServiceTest {

    @Autowired
    private SecuredService securedService;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testWithAdmin() {

        String response = securedService.onlyAdmin();

        assertThat(response).isEqualTo("Only admin can call this");
    }


    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = "SUPER_USER")
    public void testWithWrongUser() {
        securedService.onlyAdmin();
    }

}

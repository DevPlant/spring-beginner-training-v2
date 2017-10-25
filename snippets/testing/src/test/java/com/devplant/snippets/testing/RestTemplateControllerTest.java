package com.devplant.snippets.testing;

import static org.assertj.core.api.Java6Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {HttpBasicSecurity.class, TestingApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.profiles.active=http-basic")
public class RestTemplateControllerTest {

    @LocalServerPort
    private int port;

    @Test
    public void testUserEndpoint() throws Exception {

        TestRestTemplate testRestTemplate = new TestRestTemplate("timo", "timo");
        ResponseEntity<String> result = testRestTemplate.getForEntity(createTestUrl("/user"), String.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo("Only a User can access this");

    }

    @Test
    public void testUserEndpointAsAdmin() throws Exception {

        TestRestTemplate testRestTemplate = new TestRestTemplate("admin", "admin");
        ResponseEntity<String> result = testRestTemplate.getForEntity(createTestUrl("/user"), String.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

    }

    @Test
    public void testUserEndpointNoAuth() throws Exception {

        TestRestTemplate testRestTemplate = new TestRestTemplate();
        ResponseEntity<String> result = testRestTemplate.getForEntity(createTestUrl("/user"), String.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

    }

    @Test
    public void testUserEndpointNoExistingUser() throws Exception {

        TestRestTemplate testRestTemplate = new TestRestTemplate("invalid", "user");
        ResponseEntity<String> result = testRestTemplate.getForEntity(createTestUrl("/user"), String.class);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

    }

    public String createTestUrl(String uri) {
        return "http://localhost:" + port + uri;
    }
}

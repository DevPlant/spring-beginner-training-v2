package com.devplant.snippets.testing;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;


@RunWith(SpringRunner.class)
@Import(FormSecurityConfig.class)
@WebMvcTest(value = SampleController.class)
public class MockMvcControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    @WithMockUser(username = "test", password = "test", roles = {"USER"})
    public void testUserEndpoint() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = get("/user");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(mvcResult -> assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("Only a User can access this"));
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = {"ADMIN"})
    public void testUserEndpointAsAdmin() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = get("/user");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isForbidden());
    }

    @Test
    public void testUserEndpointNoAuth() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = get("/user");
        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection());
    }
}
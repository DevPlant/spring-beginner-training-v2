package com.devplant.basics.security.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.Collections;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.devplant.basics.security.configuration.WebSecurityConfig;
import com.devplant.basics.security.model.Author;
import com.devplant.basics.security.repository.AuthorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@Import(WebSecurityConfig.class)
@WebMvcTest(value = AuthorController.class)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorRepository authorRepository;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private DataSource dataSource;

    @Test
    @WithMockUser(username = "test", password = "test", roles = {"USER"})
    public void testFindAllAuthors() throws Exception {
        Author testAuthor = new Author(1, "The Dude", "The Dude's tale", null);
        when(authorRepository.findAll()).thenReturn(Collections.singletonList(testAuthor));
        MockHttpServletRequestBuilder requestBuilder = get("/api/author")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(mvcResult -> assertThat(mapper.readValue(mvcResult.getResponse().getContentAsString(), Author[].class)[0]).isEqualTo(testAuthor));
    }

    @Test
    @WithMockUser(username = "test", password = "test")
    public void testGetBookById() throws Exception {
        Author testAuthor = new Author(1, "The Dude", "The Dude's tale", null);
        when(authorRepository.findOne(1L)).thenReturn(testAuthor);
        MockHttpServletRequestBuilder requestBuilder = get("/api/author/{authorId}", 1)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(mvcResult -> assertThat(mapper.readValue(mvcResult.getResponse().getContentAsString(), Author.class)).isEqualTo(testAuthor));
    }

    @Test
    @WithMockUser
    public void testFindByWrittenBook() throws Exception {
        Author testAuthor = new Author(1, "The Dude", "The Dude's tale", null);
        when(authorRepository.findByWrittenBook("Test Book")).thenReturn(testAuthor);
        MockHttpServletRequestBuilder requestBuilder = get("/api/author/by-written-book").param("bookName", "Test Book")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(mvcResult -> assertThat(mapper.readValue(mvcResult.getResponse().getContentAsString(), Author.class)).isEqualTo(testAuthor));
    }


}
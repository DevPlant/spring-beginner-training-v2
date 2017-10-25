package com.devplant.basics.security.controller;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.Collections;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.devplant.basics.security.controller.model.SimplePage;
import com.devplant.basics.security.model.Book;
import com.devplant.basics.security.repository.BookRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(value = BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private DataSource dataSource;

    @Test
    @WithMockUser
    public void testGetAllBooks() throws Exception {
        Book testBook = new Book(1, 2017, "Test Book", "Something", "ISBN", null);
        when(bookRepository.findAll(Mockito.any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(testBook)));
        MockHttpServletRequestBuilder requestBuilder = get("/api/book")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(mvcResult -> assertThat(mapper.<SimplePage<Book>>readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<SimplePage<Book>>() {
                }).getData().get(0)).isEqualTo(testBook));
    }

    @Test
    @WithMockUser
    public void testGetBookById() throws Exception {
        Book testBook = new Book(1, 2017, "Test Book", "Something", "ISBN", null);
        when(bookRepository.findOne(1L)).thenReturn(testBook);
        MockHttpServletRequestBuilder requestBuilder = get("/api/book/{bookId}", 1)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(mvcResult -> assertThat(mapper.readValue(mvcResult.getResponse().getContentAsString(), Book.class)).isEqualTo(testBook));
    }

    @Test
    @WithMockUser
    public void testFindBookByName() throws Exception {
        Book testBook = new Book(1, 2017, "Test Book", "Something", "ISBN", null);
        when(bookRepository.findFirstByName("Test Book")).thenReturn(testBook);
        MockHttpServletRequestBuilder requestBuilder = get("/api/book/by-name").param("name", "Test Book")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(mvcResult -> assertThat(mapper.readValue(mvcResult.getResponse().getContentAsString(), Book.class)).isEqualTo(testBook));
    }

    @Test
    @WithMockUser
    public void testFindBooksByAuthorName() throws Exception {
        Book testBook = new Book(1, 2017, "Test Book", "Something", "ISBN", null);
        when(bookRepository.findByAuthorName("The Unknown")).thenReturn(Collections.singletonList(testBook));
        MockHttpServletRequestBuilder requestBuilder = get("/api/book/by-author-name").param("authorName", "The Unknown")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(mvcResult -> assertThat(mapper.readValue(mvcResult.getResponse().getContentAsString(), Book[].class)[0]).isEqualTo(testBook));
    }

}

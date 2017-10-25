package com.devplant.basics.testing.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.devplant.basics.testing.model.Book;
import com.devplant.basics.testing.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(value = BookController.class, secure = false)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void testGetAllBooks() throws Exception {
        Book testBook = new Book(1, 2017, "Test Book", "Something", "ISBN", null);
        when(bookRepository.findAll()).thenReturn(Collections.singletonList(testBook));
        MockHttpServletRequestBuilder requestBuilder = get("/api/book")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(mvcResult -> assertThat(mapper.readValue(mvcResult.getResponse().getContentAsString(), Book[].class)[0]).isEqualTo(testBook));
    }

    @Test
    public void testGetBookById() throws Exception {
        Book testBook = new Book(1, 2017, "Test Book", "Something", "ISBN", null);
        when(bookRepository.findOne(1L)).thenReturn(testBook);
        MockHttpServletRequestBuilder requestBuilder = get("/api/book/{bookId}",1)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(mvcResult -> assertThat(mapper.readValue(mvcResult.getResponse().getContentAsString(), Book.class)).isEqualTo(testBook));
    }

    @Test
    public void testFindBookByName() throws Exception {
        Book testBook = new Book(1, 2017, "Test Book", "Something", "ISBN", null);
        when(bookRepository.findOneByName("Test Book")).thenReturn(testBook);
        MockHttpServletRequestBuilder requestBuilder = get("/api/book/by-name").param("name","Test Book")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(mvcResult -> assertThat(mapper.readValue(mvcResult.getResponse().getContentAsString(), Book.class)).isEqualTo(testBook));
    }

    @Test
    public void testFindBooksByAuthorName() throws Exception {
        Book testBook = new Book(1, 2017, "Test Book", "Something", "ISBN", null);
        when(bookRepository.findByAuthorName("The Unknown")).thenReturn(Collections.singletonList(testBook));
        MockHttpServletRequestBuilder requestBuilder = get("/api/book/by-author-name").param("authorName","The Unknown")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(mvcResult -> assertThat(mapper.readValue(mvcResult.getResponse().getContentAsString(), Book[].class)[0]).isEqualTo(testBook));
    }

}

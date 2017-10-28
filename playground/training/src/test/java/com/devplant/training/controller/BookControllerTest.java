package com.devplant.training.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

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

import com.devplant.training.configuration.SecurityConfig;
import com.devplant.training.entity.Book;
import com.devplant.training.repo.AuthorRepo;
import com.devplant.training.repo.BookRepo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@Import(SecurityConfig.class)
@WebMvcTest(value = {BookController.class})
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookRepo bookRepo;

    @MockBean
    private DataSource dataSource;

    @MockBean
    private AuthorRepo authorRepo;


    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testCreateBook() throws Exception {
        Book book = Book.builder().id(1).build();

        mockMvc.perform(post("/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testDeleteBookWithBadPath() throws Exception {
        mockMvc.perform(delete("/book/asd"))
                .andExpect(status().is(400));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void testDeleteBookAsUser() throws Exception {
        mockMvc.perform(delete("/book/1"))
                .andExpect(status().is(403));
    }


    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testDeleteBookAsAdmin() throws Exception {
        mockMvc.perform(delete("/book/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "me", roles = {"ADMIN"})
    public void testFindAllBooks() throws Exception {

        List<Book> bookList = Arrays.asList(Book.builder().id(1).build());
        when(bookRepo.findAll()).thenReturn(bookList);
        mockMvc.perform(get("/book"))
                .andExpect(status().isOk())
                .andExpect(mvcResult -> {

                    List<Book> result = objectMapper.readValue(
                            mvcResult.getResponse().getContentAsString(),
                            new TypeReference<List<Book>>() {
                            });

                    Book[] cleanResult = objectMapper.readValue(
                            mvcResult.getResponse().getContentAsString(), Book[].class);

                    assertThat(bookList).isEqualTo(result);

                    assertThat(bookList).containsExactly(cleanResult);
                });


    }


}

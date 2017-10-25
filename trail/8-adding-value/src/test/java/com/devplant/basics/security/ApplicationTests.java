package com.devplant.basics.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.devplant.basics.security.configuration.LibraryConfiguration;
import com.devplant.basics.security.configuration.WebSecurityConfig;
import com.devplant.basics.security.controller.model.SimplePage;
import com.devplant.basics.security.model.Author;
import com.devplant.basics.security.model.Book;
import com.devplant.basics.security.repository.AuthorRepository;
import com.devplant.basics.security.repository.BookRepository;
import com.devplant.basics.security.service.BookManagementService;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {LibraryConfiguration.class, WebSecurityConfig.class, Application.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.profiles.active=logging-notifications")
public class ApplicationTests extends AbstractFlowTest {

    private TestRestTemplate userRestTemplate = new TestRestTemplate("user", "user");

    @Autowired
    private BookManagementService bookManagementService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Before
    public void before() {
        super.before();
        Author author = Author.builder().name("The Dude").bio("He Lived a epic life").build();
        Book book = Book.builder().name("A book").synopsis("something to describe").author(author).build();

        Book createdBook = bookManagementService.createBook(book);
        AssertionsForClassTypes.assertThat(createdBook.getId()).isGreaterThan(0);
        AssertionsForClassTypes.assertThat(createdBook.getAuthor().getId()).isGreaterThan(0);
        bookManagementService.createBook(book);
    }

    @Test
    public void testFindAllBooks() {
        ResponseEntity<SimplePage<Book>> allBooks = userRestTemplate.exchange(createTestUrl("/api/book"), HttpMethod.GET, null, new ParameterizedTypeReference<SimplePage<Book>>() {
        });
        assertThat(allBooks.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(allBooks.getBody().getData().size()).isGreaterThan(0);
        assertThat(allBooks.getBody().getData().iterator().next().getName()).isEqualTo("A book");
    }

    @Test
    public void testFindBookById() {
        long id = authorRepository.findAll().iterator().next().getId();
        ResponseEntity<Book> book = userRestTemplate.getForEntity(createTestUrl("/api/book/" + id), Book.class);
        assertThat(book.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(book.getBody().getId()).isEqualTo(id);
    }

    @Test
    public void testFindBookByName() {
        String testName = "A book";
        ResponseEntity<Book> book = userRestTemplate.getForEntity(createTestUrl("/api/book/by-name?name=" + testName), Book.class);
        assertThat(book.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(book.getBody().getName()).isEqualTo(testName);
    }

    @Test
    public void testFindBooksByAuthorName() {
        String authorName = "The Dude";
        ResponseEntity<Book[]> authorBooks = userRestTemplate.getForEntity(createTestUrl("/api/book/by-author-name?authorName=" + authorName), Book[].class);
        assertThat(authorBooks.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(authorBooks.getBody().length).isEqualTo(1);
    }

    @Test
    public void testFindAllAuthors() {
        ResponseEntity<Author[]> authors = userRestTemplate.getForEntity(createTestUrl("/api/author"), Author[].class);
        assertThat(authors.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(authors.getBody().length).isEqualTo(1);
    }

    @Test
    public void testFindAuthorById() {
        long id = authorRepository.findAll().iterator().next().getId();
        ResponseEntity<Author> author = userRestTemplate.getForEntity(createTestUrl("/api/author/" + id), Author.class);
        assertThat(author.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(author.getBody().getId()).isEqualTo(id);
    }

    @Test
    public void testFindByWrittenBook() {
        String testName = "A book";
        ResponseEntity<Author> authorByBook = userRestTemplate.getForEntity(createTestUrl("/api/author/by-written-book?bookName=" + testName), Author.class);
        assertThat(authorByBook.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(authorByBook.getBody().getName()).isEqualTo("The Dude");
    }

    @After
    public void after() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
    }
}

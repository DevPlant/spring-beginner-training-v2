package com.devplant.basics.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.devplant.basics.security.model.Author;
import com.devplant.basics.security.model.Book;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTests {

    @LocalServerPort
    private int port;

    private TestRestTemplate userRestTemplate = new TestRestTemplate("user", "user");

    private TestRestTemplate adminRestTemplate = new TestRestTemplate("admin", "admin");

    @Test
    public void testFindAllBooks() {
        ResponseEntity<Book[]> allBooks = userRestTemplate.getForEntity(createTestUrl("/api/book"), Book[].class);
        assertThat(allBooks.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(allBooks.getBody().length).isGreaterThan(0);
    }

    @Test
    public void testFindBookById() {
        ResponseEntity<Book> book = userRestTemplate.getForEntity(createTestUrl("/api/book/1"), Book.class);
        assertThat(book.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(book.getBody().getId()).isEqualTo(1);
    }

    @Test
    public void testFindBookByName() {
        String testName = "Lord of the Rings - The Fellowship of the Ring";
        ResponseEntity<Book> book = userRestTemplate.getForEntity(createTestUrl("/api/book/by-name?name=" + testName), Book.class);
        assertThat(book.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(book.getBody().getName()).isEqualTo(testName);
    }

    @Test
    public void testFindBooksByAuthorName() {
        String authorName = "J. R. R. Tolkien";
        ResponseEntity<Book[]> authorBooks = userRestTemplate.getForEntity(createTestUrl("/api/book/by-author-name?authorName=" + authorName), Book[].class);
        assertThat(authorBooks.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(authorBooks.getBody().length).isGreaterThan(1);
    }

    @Test
    public void testFindAllAuthors() {
        ResponseEntity<Author[]> authors = userRestTemplate.getForEntity(createTestUrl("/api/author"), Author[].class);
        assertThat(authors.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(authors.getBody().length).isEqualTo(2);
    }

    @Test
    public void testFindAuthorById() {
        ResponseEntity<Author> author = userRestTemplate.getForEntity(createTestUrl("/api/author/1"), Author.class);
        assertThat(author.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(author.getBody().getId()).isEqualTo(1);
    }

    @Test
    public void testFindByWrittenBook() {
        String testName = "Lord of the Rings - The Fellowship of the Ring";
        ResponseEntity<Author> authorByBook = userRestTemplate.getForEntity(createTestUrl("/api/author/by-written-book?bookName=" + testName), Author.class);
        assertThat(authorByBook.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(authorByBook.getBody().getName()).isEqualTo("J. R. R. Tolkien");
    }


    @Test
    public void testDeleteBookWithUser() {
        ResponseEntity<Void> exchangeResult = userRestTemplate.exchange(createTestUrl("/api/book/2"), HttpMethod.DELETE, null, Void.class);
        assertThat(exchangeResult.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void testDeleteBookWithAdmin() {
        ResponseEntity<Void> exchangeResult = adminRestTemplate.exchange(createTestUrl("/api/book/2"), HttpMethod.DELETE, null, Void.class);
        assertThat(exchangeResult.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testAddBookWithUser() {
        Book book = Book.builder().name("Test").isbn("Test").synopsis("Test").year(2012).build();
        ResponseEntity<Book> responseBook = userRestTemplate.postForEntity(createTestUrl("/api/book"), book, Book.class);
        assertThat(responseBook.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void testAddBookWithAdmin() {
        Book book = Book.builder().name("Test").isbn("Test").synopsis("Test").year(2012).build();
        ResponseEntity<Book> responseBook = adminRestTemplate.postForEntity(createTestUrl("/api/book"), book, Book.class);
        assertThat(responseBook.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseBook.getBody().getName()).isEqualTo("Test");
    }


    private String createTestUrl(String uri) {
        return "http://localhost:" + port + uri;
    }

}

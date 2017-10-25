package com.devplant.basics.apiexplorer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.devplant.basics.apiexplorer.model.Author;
import com.devplant.basics.apiexplorer.model.Book;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTests {

    @LocalServerPort
    private int port;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    public void testFindAllBooks() {
        ResponseEntity<Book[]> allBooks = restTemplate.getForEntity(createTestUrl("/api/book"), Book[].class);
        assertThat(allBooks.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(allBooks.getBody().length).isEqualTo(8);
    }

    @Test
    public void testFindBookById() {
        ResponseEntity<Book> book = restTemplate.getForEntity(createTestUrl("/api/book/1"), Book.class);
        assertThat(book.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(book.getBody().getId()).isEqualTo(1);
    }

    @Test
    public void testFindBookByName() {
        String testName = "Lord of the Rings - The Fellowship of the Ring";
        ResponseEntity<Book> book = restTemplate.getForEntity(createTestUrl("/api/book/by-name?name=" + testName), Book.class);
        assertThat(book.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(book.getBody().getName()).isEqualTo(testName);
    }

    @Test
    public void testFindBooksByAuthorName() {
        String authorName = "J. R. R. Tolkien";
        ResponseEntity<Book[]> authorBooks = restTemplate.getForEntity(createTestUrl("/api/book/by-author-name?authorName=" + authorName), Book[].class);
        assertThat(authorBooks.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(authorBooks.getBody().length).isEqualTo(3);
    }

    @Test
    public void testFindAllAuthors() {
        ResponseEntity<Author[]> authors = restTemplate.getForEntity(createTestUrl("/api/author"), Author[].class);
        assertThat(authors.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(authors.getBody().length).isEqualTo(2);
    }

    @Test
    public void testFindAuthorById() {
        ResponseEntity<Author> author = restTemplate.getForEntity(createTestUrl("/api/author/1"), Author.class);
        assertThat(author.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(author.getBody().getId()).isEqualTo(1);
    }

    @Test
    public void testFindByWrittenBook() {
        String testName = "Lord of the Rings - The Fellowship of the Ring";
        ResponseEntity<Author> authorByBook = restTemplate.getForEntity(createTestUrl("/api/author/by-written-book?bookName="+testName), Author.class);
        assertThat(authorByBook.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(authorByBook.getBody().getName()).isEqualTo("J. R. R. Tolkien");
    }


    private String createTestUrl(String uri) {
        return "http://localhost:" + port + uri;
    }

}

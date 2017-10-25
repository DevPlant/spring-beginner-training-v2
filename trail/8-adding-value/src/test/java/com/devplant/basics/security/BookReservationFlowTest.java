package com.devplant.basics.security;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import com.devplant.basics.security.controller.error.BookNotAvailableException;
import com.devplant.basics.security.model.Author;
import com.devplant.basics.security.model.Book;
import com.devplant.basics.security.model.BookStock;
import com.devplant.basics.security.repository.AuthorRepository;
import com.devplant.basics.security.repository.BookRepository;
import com.devplant.basics.security.repository.BookStockRepository;
import com.devplant.basics.security.service.BookManagementService;
import com.devplant.basics.security.service.BookReservationService;
import com.devplant.basics.security.service.notification.INotificationService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.profiles.active=logging-notifications")
public class BookReservationFlowTest extends AbstractFlowTest {

    @Autowired
    private BookManagementService bookManagementService;

    @Autowired
    private BookReservationService bookReservationService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookStockRepository bookStockRepository;

    @SpyBean
    private INotificationService loggingNotificationService;

    private Book createdBook;

    @Before
    public void before() {
        super.before();
        Author author = Author.builder().name("Dude").bio("He Lived a epic life").build();
        Book book = Book.builder().name("A book").synopsis("something to describe").author(author).build();

        createdBook = bookManagementService.createBook(book);
        assertThat(createdBook.getId()).isGreaterThan(0);
        assertThat(createdBook.getAuthor().getId()).isGreaterThan(0);


        bookManagementService.increaseStockCount(createdBook.getId(), 1);

        assertThat(bookStockRepository.countByBookIdAndAvailableTrue(createdBook.getId())).isEqualTo(1);
    }

    @Test
    @WithMockUser(value = "admin", roles = "ADMIN")
    public void testBookReservationIdealFlow() {

        BookStock bookStock = bookReservationService.requestBook(createdBook.getId());
        assertThat(bookStock).isNotNull();
        assertThat(bookStock.getUser().getUsername()).isEqualTo("admin");

        BookStock approved = bookReservationService.approveRequest(bookStock.getId());
        assertThat(approved.isApproved()).isTrue();
        assertThat(approved.getId()).isEqualTo(bookStock.getId());

        verify(loggingNotificationService).notifyUserAboutApproval(createdBook.getName(), "admin", approved.getId());

        BookStock pickedUp = bookReservationService.bookPickedUp(approved.getId());
        assertThat(pickedUp.isPickedUp()).isTrue();
        assertThat(pickedUp.getId()).isEqualTo(bookStock.getId());

        verify(loggingNotificationService).notifyUserAboutPickup(createdBook.getName(), "admin", approved.getId());

        BookStock returnedStock = bookReservationService.bookReturned(pickedUp.getId());

        assertThat(returnedStock.isAvailable()).isTrue();
        assertThat(returnedStock.getUser()).isNull();

        BookStock newRequest = bookReservationService.requestBook(createdBook.getId());
        assertThat(newRequest).isNotNull();
        assertThat(newRequest.getUser().getUsername()).isEqualTo("admin");

    }

    @Test(expected = BookNotAvailableException.class)
    @WithMockUser(value = "admin", roles = "ADMIN")
    public void testBookReservationIdealNotAvailable() {
        bookReservationService.requestBook(createdBook.getId());
        bookReservationService.requestBook(createdBook.getId());
    }

    @After
    public void after() {
        bookStockRepository.deleteAll();
        bookRepository.deleteAll();
        authorRepository.deleteAll();
    }


}

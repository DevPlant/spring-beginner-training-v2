package com.devplant.basics.security;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import com.devplant.basics.security.model.Author;
import com.devplant.basics.security.model.Book;
import com.devplant.basics.security.model.BookStock;
import com.devplant.basics.security.model.User;
import com.devplant.basics.security.repository.AuthorRepository;
import com.devplant.basics.security.repository.BookRepository;
import com.devplant.basics.security.repository.BookStockRepository;
import com.devplant.basics.security.repository.UserRepository;
import com.devplant.basics.security.service.BookManagementService;
import com.devplant.basics.security.service.ReservationCheckService;
import com.devplant.basics.security.service.notification.INotificationService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.profiles.active=logging-notifications")
public class ReservationCheckServiceTest extends AbstractFlowTest {

    @Autowired
    private BookManagementService bookManagementService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ReservationCheckService reservationCheckService;

    @Autowired
    private BookStockRepository bookStockRepository;

    @Autowired
    private UserRepository userRepository;

    @SpyBean
    private INotificationService loggingNotificationService;

    private Book createdBook;

    private BookStock firstStock;

    private BookStock secondStock;

    @Before
    @WithMockUser(value = "admin", roles = "ADMIN")
    public void before() {
        super.before();

        User user = User.builder().username("user").password(passwordEncoder.encode("user")).roles(Arrays.asList("ROLE_USER")).enabled(true).build();

        User savedUser = userRepository.save(user);

        Author author = Author.builder().name("The Dude").bio("He Lived a epic life").build();
        Book book = Book.builder().name("A book").synopsis("something to describe").author(author).build();

        createdBook = bookManagementService.createBook(book);
        assertThat(createdBook.getId()).isGreaterThan(0);
        assertThat(createdBook.getAuthor().getId()).isGreaterThan(0);

        bookManagementService.increaseStockCount(createdBook.getId(), 2);

        assertThat(bookStockRepository.countByBookIdAndAvailableTrue(createdBook.getId())).isEqualTo(2);

        List<BookStock> currentStocks = bookStockRepository.findByBookId(createdBook.getId());

        firstStock = currentStocks.get(0);

        firstStock.setAvailable(false);
        firstStock.setApproved(true);
        firstStock.setPickedUp(false);
        firstStock.setUser(savedUser);
        firstStock.setLatestPickupDate(LocalDateTime.now().minusDays(5));

        secondStock = currentStocks.get(1);

        secondStock.setAvailable(false);
        secondStock.setApproved(true);
        secondStock.setPickedUp(true);
        secondStock.setUser(savedUser);
        secondStock.setLatestReturnDate(LocalDateTime.now().minusDays(5));

        bookStockRepository.save(currentStocks);

    }

    @Test
    @WithMockUser(value = "admin", roles = "ADMIN")
    public void testBookReservationIdealFlow() {

        reservationCheckService.checkReservations();

        verify(loggingNotificationService).notifyUserAboutPickupExpired(createdBook.getName(), "user", firstStock.getId());

        verify(loggingNotificationService).notifyUserAboutReturnDateExpired(createdBook.getName(), "user", secondStock.getId());
    }


    @After
    public void after() {
        bookStockRepository.deleteAll();
        bookRepository.deleteAll();
        authorRepository.deleteAll();
    }


}

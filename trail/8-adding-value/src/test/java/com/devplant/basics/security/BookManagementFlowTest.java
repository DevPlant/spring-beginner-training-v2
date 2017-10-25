package com.devplant.basics.security;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import com.devplant.basics.security.configuration.LibraryConfiguration;
import com.devplant.basics.security.configuration.WebSecurityConfig;
import com.devplant.basics.security.controller.error.InvalidRequestException;
import com.devplant.basics.security.model.Author;
import com.devplant.basics.security.model.Book;
import com.devplant.basics.security.model.BookStock;
import com.devplant.basics.security.repository.BookRepository;
import com.devplant.basics.security.repository.BookStockRepository;
import com.devplant.basics.security.service.BookManagementService;
import com.devplant.basics.security.service.notification.INotificationService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {LibraryConfiguration.class, WebSecurityConfig.class, Application.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.profiles.active=logging-notifications")
public class BookManagementFlowTest extends AbstractFlowTest {

    @Autowired
    private BookManagementService bookManagementService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookStockRepository bookStockRepository;

    @SpyBean
    private INotificationService loggingNotificationService;

    @Test
    @WithMockUser(value = "admin", roles = "ADMIN")
    public void testBookManagementFlow() {

        Author author = Author.builder().name("The Dude").bio("He Lived a epic life").build();
        Book book = Book.builder().name("A book").synopsis("something to describe").author(author).build();

        Book createdBook = bookManagementService.createBook(book);
        assertThat(createdBook.getId()).isGreaterThan(0);
        assertThat(createdBook.getAuthor().getId()).isGreaterThan(0);


        bookManagementService.increaseStockCount(createdBook.getId(), 10);

        assertThat(bookStockRepository.countByBookIdAndAvailableTrue(createdBook.getId())).isEqualTo(10);

        bookManagementService.decreaseStockCount(createdBook.getId(), 5, false, false);

        assertThat(bookStockRepository.countByBookIdAndAvailableTrue(createdBook.getId())).isEqualTo(5);

        List<BookStock> currentStocks = bookStockRepository.findByBookId(createdBook.getId());

        // mark the rest as not available / picked-up
        for (int i = 0; i < currentStocks.size(); i++) {
            currentStocks.get(i).setAvailable(false);
            if (i % 2 == 0) {
                currentStocks.get(i).setPickedUp(true);
            }
        }

        BookStock firstStock = currentStocks.get(0);
        firstStock.setPickedUp(false);
        firstStock.setUser(userRepository.findOne("user"));

        BookStock secondStock = currentStocks.get(1);
        secondStock.setPickedUp(true);
        secondStock.setUser(userRepository.findOne("user"));

        bookStockRepository.save(currentStocks);

        bookManagementService.resetBookStock(secondStock.getId());
        verify(loggingNotificationService).notifyUserAboutBadBehaviour(createdBook.getName(), "user", secondStock.getId());
        // force delete it;
        try {
            bookManagementService.deleteBook(createdBook.getId(), false);
            // We shouldn't reach this since there's an exception
            assertThat(true).isFalse();
        } catch (InvalidRequestException e) {
            assertThat(e).isInstanceOf(InvalidRequestException.class);
        }

        bookManagementService.deleteBook(createdBook.getId(), true);

        assertThat(bookRepository.findOne(createdBook.getId())).isEqualTo(null);

        verify(loggingNotificationService).notifyUserAboutCancel(createdBook.getName(), "user", firstStock.getId());

    }


}

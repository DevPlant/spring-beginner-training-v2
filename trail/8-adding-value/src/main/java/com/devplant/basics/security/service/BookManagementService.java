package com.devplant.basics.security.service;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devplant.basics.security.controller.error.InvalidRequestException;
import com.devplant.basics.security.controller.error.NotFoundException;
import com.devplant.basics.security.model.Book;
import com.devplant.basics.security.model.BookStock;
import com.devplant.basics.security.repository.AuthorRepository;
import com.devplant.basics.security.repository.BookRepository;
import com.devplant.basics.security.repository.BookStockRepository;
import com.devplant.basics.security.service.notification.INotificationService;

@Service
public class BookManagementService {

    @Autowired
    private BookStockRepository bookStockRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private List<INotificationService> notificationServices;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void increaseStockCount(long bookId, @Min(0L) int count) {
        Book book = bookRepository.findOne(bookId);
        if (book == null) {
            throw new NotFoundException("Book with id " + bookId + " not found");
        }
        List<BookStock> stocks = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            stocks.add(BookStock.builder().book(book).available(true).build());
        }
        bookStockRepository.save(stocks);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void decreaseStockCount(long bookId, @Min(0L) int requiredDeleteCount, boolean removeApproved, boolean forceRemove) {
        Book book = bookRepository.findOne(bookId);
        if (book == null) {
            throw new NotFoundException("Book with id " + bookId + " not found");
        }
        List<BookStock> stocks = bookStockRepository.findByBookId(bookId);

        List<BookStock> stocksToDelete = new ArrayList<>();
        for (BookStock bookStock : stocks) {
            if (bookStock.isAvailable() && requiredDeleteCount > 0) {
                stocksToDelete.add(bookStock);
                requiredDeleteCount -= 1;
            }
        }

        // still need to remove book stocks, remove the ones that are not picked up
        if (requiredDeleteCount != 0 && removeApproved) {
            for (BookStock bookStock : stocks) {
                if (!bookStock.isAvailable() && !bookStock.isPickedUp() && requiredDeleteCount > 0) {
                    stocksToDelete.add(bookStock);
                    requiredDeleteCount -= 1;
                }
            }
        }

        // still need to remove book stocks, remove even the ones that are picked up
        if (requiredDeleteCount != 0 && forceRemove) {
            for (BookStock bookStock : stocks) {
                if (bookStock.isPickedUp() && requiredDeleteCount > 0) {
                    stocksToDelete.add(bookStock);
                    requiredDeleteCount -= 1;
                }
            }
        }

        if (requiredDeleteCount != 0) {
            throw new InvalidRequestException("Could not delete required amount of book stocks!");
        } else {
            stocksToDelete.forEach(bookStock -> {
                if (bookStock.getUser() != null) {
                    notificationServices.forEach(service -> service.notifyUserAboutCancel(bookStock.getBook().getName(), bookStock.getUser().getUsername(), bookStock.getId()));
                }
            });
            bookStockRepository.delete(stocksToDelete);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteBook(long bookId, boolean forceDelete) {
        Book book = bookRepository.findOne(bookId);
        if (book != null) {
            int stockCount = bookStockRepository.countByBookId(bookId);
            if (stockCount == 0) {
                // delete if there are no stocks
                bookRepository.delete(bookId);
            } else {
                decreaseStockCount(bookId, stockCount, true, forceDelete);
                bookRepository.delete(bookId);
            }
        } else {
            throw new NotFoundException("Book with id " + bookId + " not found");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void resetBookStock(long bookStockId) {
        BookStock bookStock = bookStockRepository.findOne(bookStockId);
        if (bookStock.getUser() != null && bookStock.isPickedUp()) {
            notificationServices.forEach(service -> service.notifyUserAboutBadBehaviour(bookStock.getBook().getName(), bookStock.getUser().getUsername(), bookStock.getId()));
        }
        bookStock.setUser(null);
        bookStock.setLatestReturnDate(null);
        bookStock.setLatestPickupDate(null);
        bookStock.setRequestDate(null);
        bookStock.setAvailable(true);
        bookStock.setApproved(false);
        bookStock.setPickedUp(false);
        bookStock.setOverdue(false);
        bookStock.setLastReturnOverDueNotificationDate(null);

        bookStockRepository.save(bookStock);
    }

    public Book createBook(Book book) {
        if (book.getAuthor() != null) {
            authorRepository.save(book.getAuthor());
        }
        return bookRepository.save(book);
    }
}

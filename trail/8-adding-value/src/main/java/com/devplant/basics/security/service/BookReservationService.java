package com.devplant.basics.security.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devplant.basics.security.controller.error.BookNotAvailableException;
import com.devplant.basics.security.controller.error.InvalidRequestException;
import com.devplant.basics.security.controller.error.NotFoundException;
import com.devplant.basics.security.model.Book;
import com.devplant.basics.security.model.BookStock;
import com.devplant.basics.security.model.User;
import com.devplant.basics.security.repository.BookRepository;
import com.devplant.basics.security.repository.BookStockRepository;
import com.devplant.basics.security.repository.UserRepository;
import com.devplant.basics.security.service.notification.INotificationService;

@Service
public class BookReservationService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookStockRepository bookStockRepository;

    @Autowired
    private List<INotificationService> notificationServices;

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Transactional
    public BookStock requestBook(long bookId) {
        Book book = bookRepository.findOne(bookId);
        if (book == null) {
            throw new NotFoundException("Book with id " + bookId + " not found");
        }
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();

        BookStock availableStock = bookStockRepository.findOneByBookIdAndAvailableTrue(book.getId());
        if (availableStock == null) {
            throw new BookNotAvailableException("Book not available for renting!");
        }

        User user = userRepository.findOne(currentUserName);
        availableStock.setUser(user);
        availableStock.setAvailable(false);
        availableStock.setRequestDate(LocalDateTime.now());

        return bookStockRepository.save(availableStock);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public BookStock approveRequest(long bookStockId) {
        BookStock bookStock = bookStockRepository.findOne(bookStockId);
        if (bookStock == null) {
            throw new NotFoundException("BookStock with id: " + bookStockId + " not found!");
        } else if (bookStock.getUser() != null) {
            bookStock.setApproved(true);
            bookStock.setLatestPickupDate(LocalDateTime.now().plusDays(3));
            notificationServices.forEach(s -> s.notifyUserAboutApproval(bookStock.getBook().getName(), bookStock.getUser().getUsername(), bookStock.getId()));
            return bookStockRepository.save(bookStock);
        } else {
            throw new InvalidRequestException("BookStock not reserved");
        }
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public BookStock bookPickedUp(long bookStockId) {
        BookStock bookStock = bookStockRepository.findOne(bookStockId);
        if (bookStock == null) {
            throw new NotFoundException("BookStock with id: " + bookStockId + " not found!");
        } else if (bookStock.getUser() != null) {
            bookStock.setPickedUp(true);
            bookStock.setLatestReturnDate(LocalDateTime.now().plusDays(3));
            bookStock.setLastReturnOverDueNotificationDate(null);
            notificationServices.forEach(s -> s.notifyUserAboutPickup(bookStock.getBook().getName(), bookStock.getUser().getUsername(), bookStock.getId()));
            return bookStockRepository.save(bookStock);
        } else {
            throw new InvalidRequestException("BookStock cannot be picked up, no user assigned!");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public BookStock bookReturned(long bookStockId) {
        BookStock bookStock = bookStockRepository.findOne(bookStockId);
        if (bookStock == null) {
            throw new NotFoundException("BookStock with id: " + bookStockId + " not found!");
        } else if (bookStock.getUser() != null) {
            bookStock.setUser(null);
            bookStock.setLatestReturnDate(null);
            bookStock.setLatestPickupDate(null);
            bookStock.setLastReturnOverDueNotificationDate(null);
            bookStock.setRequestDate(null);
            bookStock.setAvailable(true);
            bookStock.setApproved(false);
            bookStock.setPickedUp(false);
            return bookStockRepository.save(bookStock);
        } else {
            throw new InvalidRequestException("BookStock cannot be returned, no user assigned!");
        }
    }
}

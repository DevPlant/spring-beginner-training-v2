package com.devplant.basics.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.devplant.basics.security.controller.error.BookNotAvailableException;
import com.devplant.basics.security.controller.model.ErrorResponse;
import com.devplant.basics.security.model.BookStock;
import com.devplant.basics.security.repository.BookStockRepository;
import com.devplant.basics.security.service.BookReservationService;

@RestController
@RequestMapping("/api/reservation")
public class BookReservationController {

    @Autowired
    private BookStockRepository bookStockRepository;

    @Autowired
    private BookReservationService bookReservationService;

    @GetMapping("/check-availability")
    public boolean checkAvailability(@RequestParam("bookId") long bookId) {
        return bookStockRepository.countByBookIdAndAvailableTrue(bookId) > 0;
    }

    @PostMapping("/request-book")
    public BookStock requestBook(@RequestParam("bookId") long bookId) {
        return bookReservationService.requestBook(bookId);
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler(value = BookNotAvailableException.class)
    @ResponseBody
    public ErrorResponse handleBookNotAvailableException(BookNotAvailableException e) {
        return new ErrorResponse(e.getMessage());
    }

}

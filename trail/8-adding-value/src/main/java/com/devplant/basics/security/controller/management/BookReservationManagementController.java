package com.devplant.basics.security.controller.management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devplant.basics.security.model.BookStock;
import com.devplant.basics.security.service.BookReservationService;

@RestController
@RequestMapping("/api/admin/reservation")
public class BookReservationManagementController {

    @Autowired
    private BookReservationService bookReservationService;

    @PostMapping("/approve-request")
    public BookStock approveRequest(@RequestParam("bookStockId") long bookStockId) {
        return bookReservationService.approveRequest(bookStockId);
    }

    @PostMapping("/picked-up")
    public BookStock bookPickedUp(@RequestParam("bookStockId") long bookStockId) {
        return bookReservationService.bookPickedUp(bookStockId);
    }

    @PostMapping("/returned")
    public BookStock bookReturned(@RequestParam("bookStockId") long bookStockId) {
        return bookReservationService.bookReturned(bookStockId);
    }

}

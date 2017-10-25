package com.devplant.basics.security.controller.management;

import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devplant.basics.security.model.Book;
import com.devplant.basics.security.service.BookManagementService;

@RestController
@RequestMapping("/api/admin/book")
public class BookManagementController {

    @Autowired
    private BookManagementService bookManagementService;

    @PostMapping("/increase-stock")
    public void increaseStock(@PathVariable("bookId") long bookId, @RequestParam("count") @Min(0L) int count) {
        bookManagementService.increaseStockCount(bookId, count);
    }

    @PostMapping("/decrease-stock")
    public void decreaseStock(@PathVariable("bookId") long bookId,
                              @RequestParam("count") @Min(0L) int count,
                              @RequestParam(value = "removeApproved", required = false, defaultValue = "false") boolean removeApproved,
                              @RequestParam(value = "removeApproved", required = false, defaultValue = "false") boolean forceRemove) {
        bookManagementService.decreaseStockCount(bookId, count, removeApproved, forceRemove);
    }

    @PostMapping("")
    public Book addOrUpdateBook(@RequestBody Book book) {
        return bookManagementService.createBook(book);
    }

    @DeleteMapping("/{bookId}")
    public void deleteBook(@PathVariable("bookId") long bookId, @RequestParam(value = "force", required = false, defaultValue = "false") boolean force) {
        bookManagementService.deleteBook(bookId, force);
    }
}

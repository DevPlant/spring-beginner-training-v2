package com.devplant.basics.security.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devplant.basics.security.controller.model.SimplePage;
import com.devplant.basics.security.model.Book;
import com.devplant.basics.security.repository.BookRepository;

@RestController
@RequestMapping("/api/book")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    public SimplePage<Book> getBooks(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                               @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        Page<Book> springPage = bookRepository.findAll(new PageRequest(page, pageSize));

        return SimplePage.<Book>builder().data(springPage.getContent()).page(page).pageSize(springPage.getSize()).totalHits(springPage.getTotalElements()).build();
    }

    @GetMapping("/{bookId}")
    public Book findBookById(@PathVariable("bookId") long bookId) {
        return bookRepository.findOne(bookId);
    }

    @GetMapping("/by-name")
    public Book findBookByName(@RequestParam("name") String name) {
        return bookRepository.findFirstByName(name);
    }

    @GetMapping("/by-author-name")
    public List<Book> findBooksByAuthorName(@RequestParam("authorName") String authorName) {
        return bookRepository.findByAuthorName(authorName);
    }


}

package com.devplant.basics.security.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devplant.basics.security.model.Book;
import com.devplant.basics.security.repository.BookRepository;

@RestController
@RequestMapping("/api/book")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/{bookId}")
    public Book findBookById(@PathVariable("bookId") long bookId) {
        return bookRepository.findOne(bookId);
    }

    @GetMapping("/by-name")
    public Book findBookByName(@RequestParam("name") String name) {
        return bookRepository.findOneByName(name);
    }

    @GetMapping("/by-author-name")
    public List<Book> findBooksByAuthorName(@RequestParam("authorName") String authorName) {
        return bookRepository.findByAuthorName(authorName);
    }

    @PostMapping
    public Book addOrUpdateBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @DeleteMapping("/{bookId}")
    public void deleteBook(@PathVariable("bookId") long bookId) {
        bookRepository.delete(bookId);
    }
}

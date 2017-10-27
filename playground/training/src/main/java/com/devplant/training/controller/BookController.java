package com.devplant.training.controller;

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

import com.devplant.training.entity.Book;
import com.devplant.training.exceptions.ObjectNotFoundException;
import com.devplant.training.repo.AuthorRepo;
import com.devplant.training.repo.BookRepo;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private AuthorRepo authorRepo;

    @GetMapping
    public List<Book> findAll() {
        return bookRepo.findAll();
    }

    @PostMapping
    public Book addOrUpdate(@RequestBody Book book) {
        return bookRepo.save(book);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable("id") long id) {
        bookRepo.delete(id);
    }

    @GetMapping("/{id}")
    public Book findById(@PathVariable("id") long id) {
        return bookRepo.findOne(id);
    }

    @GetMapping("/by-author")
    public List<Book> findByAuthorName(@RequestParam("author") String authorName) {

        List<Book> allBooks = bookRepo.findAll();

//        return allBooks.stream()
//                .filter(book -> book.getAuthor().getName().equals(authorName))
//                .collect(Collectors.toList());
        return allBooks;
    }


    @GetMapping("/author-books")
    public List<Book> book(@RequestParam("author") String author) {
        return authorRepo.findByName(author).orElseThrow(()->
                new ObjectNotFoundException(
                        "Author: " + author + " does not exist")).getBooks();
    }

    @GetMapping("/by-author/v2")
    public List<Book> findByAuthorNameV2(@RequestParam("author") String authorName) {
        return bookRepo.findByAuthorName(authorName);
    }

    @GetMapping("/by-title")
    public Book byTitle(@RequestParam("title") String title) {
        return bookRepo.findNativeForTitle(title);
    }

    @GetMapping("/by-author/jpql")
    public List<Book> byAuthorJpa(@RequestParam("author") String author) {
        return bookRepo.findCustomByAuthor(author);
    }

}

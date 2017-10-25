package com.devplant.basics.persistence.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devplant.basics.persistence.model.Author;
import com.devplant.basics.persistence.repository.AuthorRepository;

@RestController
@RequestMapping("/api/author")
public class AuthorController {

    @Autowired
    private AuthorRepository authorRepository;

    @GetMapping
    public List<Author> findAllAuthors() {
        return authorRepository.findAll();
    }


    @GetMapping("/{authorId}")
    public Author findAuthorById(@PathVariable("authorId") long authorId) {
        return authorRepository.findOne(authorId);
    }

    @GetMapping("/by-written-book")
    public Author findByWrittenBook(@RequestParam("bookName") String bookName) {
        return authorRepository.findByWrittenBook(bookName);
    }

}

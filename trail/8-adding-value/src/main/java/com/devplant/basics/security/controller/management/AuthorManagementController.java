package com.devplant.basics.security.controller.management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devplant.basics.security.model.Author;
import com.devplant.basics.security.repository.AuthorRepository;

@RestController
@RequestMapping("/api/admin/author")
public class AuthorManagementController {

    @Autowired
    private AuthorRepository authorRepository;

    @PostMapping
    public Author addOrUpdateAuthor(@RequestBody Author author) {
        return authorRepository.save(author);
    }

    @DeleteMapping("/{authorId}")
    public void deleteAuthor(@PathVariable("authorId") long authorId) {
        authorRepository.delete(authorId);
    }
}

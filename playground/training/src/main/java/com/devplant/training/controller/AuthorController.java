package com.devplant.training.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devplant.training.entity.Author;
import com.devplant.training.entity.Book;
import com.devplant.training.exceptions.ObjectNotFoundException;
import com.devplant.training.repo.AuthorRepo;

@RestController
@RequestMapping("/author")
public class AuthorController {

    @Autowired
    private AuthorRepo authorRepo;

    @GetMapping
    public List<Author> allAuthors() {
        return authorRepo.findAll();
    }

    @PostMapping(consumes = "application/json")
    public Author addOrUpdateAuthor(@RequestBody Author author) {
        return authorRepo.save(author);
    }

    @GetMapping("/{authorId}")
    public Author findOne(@PathVariable("authorId") long authorId) {

        return Optional.of(authorRepo.findOne(authorId)).orElseThrow(() ->
                new ObjectNotFoundException(
                        "Author: " + authorId + " does not exist"));

    }

    @GetMapping("/by-name")
    public Author findByName(@RequestParam("name") String name) {

        return authorRepo.findByName(name).orElseThrow(() ->
                new ObjectNotFoundException(
                        "Author: " + name + " does not exist"));
    }

    @DeleteMapping("/{authorId}")
    public void deleteById(@PathVariable("authorId") long id) {
        authorRepo.delete(id);
    }


    @PostMapping("/{authorId}")
    @Transactional
    // Try the same code without the @Transactional annotation
    public Author updateBioInTransaction(@PathVariable("authorId") long authorId, @RequestParam("bio") String newBio) {
        Author author = authorRepo.findOne(authorId);
        author.setBio(newBio);
        return author;
    }

    @GetMapping("/{authorId}/books")
    // for this example set spring.jpa.open-in-view: false in your application management
    // test with @Transactional annotation and without
    public List<Book> getBooksByAuthor(@PathVariable("authorId") long authorId){

        List<Book> books =  authorRepo.findOne(authorId).getBooks();
        // this is how the proxy actually works, you can call get, it will return a proxied List,
        // any future call on that list or its elements will load the list, NOT the call to getBooks() itsself
        books.size();
        return  books;
    }

}

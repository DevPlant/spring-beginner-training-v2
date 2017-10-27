package com.devplant.training.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/author")
public class AuthorController {

    @Autowired
    private AuthorRepo authorRepo;

    @Autowired
    private CacheManager cacheManager;

    @GetMapping
    public List<Author> allAuthors() {

        List<Author> allAuthors = authorRepo.findAll();
        allAuthors.forEach(a ->
        {

            Cache cache = cacheManager.getCache("single-author");
            if (cache != null) {
                Cache.ValueWrapper inCache = cache.get(a.getId());

                if (inCache != null && inCache.get() != null) {
                    log.info(" object : " + a.getId() + " is in cache");
                } else {
                    log.info(" object : " + a.getId() + " is NOT in cache");
                }
            }

        });

        log.info(" -----> Loading from repository");
        return authorRepo.findAll();
    }

    @CacheEvict(value = "single-author", key = "#author.id")
    @PostMapping(consumes = "application/json")
    public Author addOrUpdateAuthor(@RequestBody Author author) {
        log.info("Saving Author ...");
        return authorRepo.save(author);
    }

    @GetMapping("/{authorId}")
    @Cacheable(value = "single-author", key = "#authorId")
    public Author findOne(@PathVariable("authorId") long authorId) {
        log.info("Getting author by id: " + authorId);
        return Optional.ofNullable(authorRepo.findOne(authorId)).orElseThrow(() ->
                new ObjectNotFoundException(
                        "Author: " + authorId + " does not exist"));

    }

    @DeleteMapping("/{authorId}")
    @CacheEvict(value = "single-author", key = "#id")
    public void deleteById(@PathVariable("authorId") long id) {
        log.info("Deleting author : " + id);
        authorRepo.delete(id);
    }

    @GetMapping("/by-name")
    public Author findByName(@RequestParam("name") String name) {

        return authorRepo.findByName(name).orElseThrow(() ->
                new ObjectNotFoundException(
                        "Author: " + name + " does not exist"));
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
    @Transactional
    public List<Book> getFromAuthor(@PathVariable("authorId") long id) {
        Author author = authorRepo.findOne(id);

        author.getBooks().forEach(book -> {
            log.info(author.getName() + " has written book " + book.getTitle());
        });

        return author.getBooks();
    }

    @PostMapping("/{authorId}/bio")
    public Author updateBio(@PathVariable("authorId") long id,
                            @RequestParam("bio") String bio) {

        Author author = authorRepo.findOne(id);
        author.setBio(bio);
        return authorRepo.save(author);

    }

}

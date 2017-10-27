package com.devplant.training.init;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import com.devplant.training.entity.Author;
import com.devplant.training.entity.Book;
import com.devplant.training.repo.AuthorRepo;
import com.devplant.training.repo.BookRepo;

@Service
public class DatabaseInit implements CommandLineRunner {

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private AuthorRepo authorRepo;

    @Override
    public void run(String... strings) throws Exception {

        Author a1 = Author.builder().name("Timo").bio("He's great").build();
        Author a2 = Author.builder().name("Radu").bio("He's also great").build();

        Book b1 = Book.builder().author(a1).title("The greatest book eva!").year(2042).build();
        Book b2 = Book.builder().author(a1).title("A pretty good book").year(2012).build();
        Book b3 = Book.builder().author(a2).title("Plagiatoru'").year(1990).build();

        authorRepo.save(Arrays.asList(a1, a2));
        bookRepo.save(Arrays.asList(b1, b2, b3));

    }
}

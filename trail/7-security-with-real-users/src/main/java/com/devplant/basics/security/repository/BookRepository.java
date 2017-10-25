package com.devplant.basics.security.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devplant.basics.security.model.Book;


public interface BookRepository extends JpaRepository<Book, Long> {

    Book findOneByName(String name);

    List<Book> findByAuthorName(String authorName);
}

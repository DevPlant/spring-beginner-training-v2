package com.devplant.basics.security.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.devplant.basics.security.model.Author;


public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("select distinct a from Author a where a.id in ( select distinct b.author.id from a.authoredBooks b where b.name = ?1)")
    Author findByWrittenBook(String bookName);

}

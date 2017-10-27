package com.devplant.training.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.devplant.training.entity.Book;

public interface BookRepo extends JpaRepository<Book, Long> {

    List<Book> findByAuthorName(String author);

    @Query("SELECT b from Book b where b.author.name = ?1")
    List<Book> findCustomByAuthor(String author);

    @Query(nativeQuery = true,
            value="select * from books where book_title = :title limit 1")
    Book findNativeForTitle(@Param("title") String title);
}

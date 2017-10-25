package com.devplant.basics.security.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devplant.basics.security.model.BookStock;

public interface BookStockRepository extends JpaRepository<BookStock, Long> {

    List<BookStock> findByBookId(long bookId);

    int countByBookId(long bookId);

    int countByBookIdAndAvailableTrue(long bookId);

    BookStock findOneByBookIdAndAvailableTrue(long bookId);
}

package com.devplant.training.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devplant.training.entity.Author;

public interface AuthorRepo extends JpaRepository<Author, Long> {

    Optional<Author> findByName(String author);

    Optional<Author> findById(long id);
}

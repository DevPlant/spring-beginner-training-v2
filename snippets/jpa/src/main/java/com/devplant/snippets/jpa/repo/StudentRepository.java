package com.devplant.snippets.jpa.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devplant.snippets.jpa.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByTrainerName(String name);

}

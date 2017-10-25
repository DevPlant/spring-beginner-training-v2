package com.devplant.snippets.jpa.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.devplant.snippets.jpa.model.Trainer;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    Trainer findByName(String name);


    @Query(nativeQuery = true, value = "SELECT id,name FROM trainer WHERE name = ?1")
    Trainer findByNameUsingNativeQuery(String name);

    @Query("SELECT t from Trainer t where t.name = ?1")
    Trainer findByNameUsingJpaQuery(String name);

}

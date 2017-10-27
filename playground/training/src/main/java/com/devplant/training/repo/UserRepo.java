package com.devplant.training.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devplant.training.entity.Account;

public interface UserRepo
        extends JpaRepository<Account, String> {
}

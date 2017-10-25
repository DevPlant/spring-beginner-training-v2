package com.devplant.basics.security.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.devplant.basics.security.model.User;

public interface UserRepository extends JpaRepository<User, String> {
}

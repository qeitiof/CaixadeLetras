package com.example.ApiLetter.repository;

import com.example.ApiLetter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    User findByUsername(String username); // <-- AQUI
}

package com.example.ApiLetter.repository;

import com.example.ApiLetter.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
package com.example.ApiLetter.repository;

import com.example.ApiLetter.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review> {
    List<Review> findByImdbId(String imdbId);
    
    @Query("SELECT r FROM Review r WHERE r.user.id = :userId AND r.imdbId = :imdbId")
    Review findByUserIdAndImdbId(@Param("userId") Long userId, @Param("imdbId") String imdbId);
    
    List<Review> findByUserId(Long userId);
}
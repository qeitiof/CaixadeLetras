package com.example.ApiLetter.repository;

import com.example.ApiLetter.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Movie findByImdbId(String imdbId);
}

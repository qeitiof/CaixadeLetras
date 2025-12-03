package com.example.ApiLetter.repository;

import com.example.ApiLetter.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {
    Movie findByImdbId(String imdbId);
    
    @Query("SELECT m FROM Movie m WHERE LOWER(m.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))")
    List<Movie> findByTituloContainingIgnoreCase(@Param("titulo") String titulo);
}

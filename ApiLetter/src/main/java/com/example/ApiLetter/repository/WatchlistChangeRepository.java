package com.example.ApiLetter.repository;

import com.example.ApiLetter.model.WatchlistChange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchlistChangeRepository extends JpaRepository<WatchlistChange, Long> {
}

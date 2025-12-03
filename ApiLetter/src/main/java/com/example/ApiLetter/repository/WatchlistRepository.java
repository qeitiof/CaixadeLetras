package com.example.ApiLetter.repository;

import com.example.ApiLetter.model.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
    List<Watchlist> findByUserId(Long userId);
    
    // Buscar watchlists inativas por mais de uma semana
    @Query("SELECT w FROM Watchlist w WHERE w.active = false AND w.lastUpdate < :oneWeekAgo")
    List<Watchlist> findInactiveForMoreThanOneWeek(@Param("oneWeekAgo") LocalDateTime oneWeekAgo);
    
    // Buscar watchlists ativas
    List<Watchlist> findByActiveTrue();
    
    // Buscar watchlists inativas
    List<Watchlist> findByActiveFalse();
}

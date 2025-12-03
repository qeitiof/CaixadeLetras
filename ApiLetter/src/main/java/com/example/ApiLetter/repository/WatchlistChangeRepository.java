package com.example.ApiLetter.repository;

import com.example.ApiLetter.model.Watchlist;
import com.example.ApiLetter.model.WatchlistChange;
import com.example.ApiLetter.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchlistChangeRepository extends JpaRepository<WatchlistChange, Long> {
    List<WatchlistChange> findByWatchlistId(Long watchlistId);
    List<WatchlistChange> findByWatchlistIdOrderByCreatedAtDesc(Long watchlistId);
    WatchlistChange findByWatchlistAndMovie(Watchlist watchlist, Movie movie);
    List<WatchlistChange> findByWatchlistAndAction(Watchlist watchlist, String action);
}

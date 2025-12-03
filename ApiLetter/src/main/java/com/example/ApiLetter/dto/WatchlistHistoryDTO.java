package com.example.ApiLetter.dto;

import java.time.LocalDateTime;

public class WatchlistHistoryDTO {
    private Long id;
    private String action; // ADD ou REMOVE
    private LocalDateTime createdAt;
    private WatchlistItemDTO movie;
    private String watchlistName;

    public WatchlistHistoryDTO() {}

    public WatchlistHistoryDTO(Long id, String action, LocalDateTime createdAt, WatchlistItemDTO movie, String watchlistName) {
        this.id = id;
        this.action = action;
        this.createdAt = createdAt;
        this.movie = movie;
        this.watchlistName = watchlistName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public WatchlistItemDTO getMovie() {
        return movie;
    }

    public void setMovie(WatchlistItemDTO movie) {
        this.movie = movie;
    }

    public String getWatchlistName() {
        return watchlistName;
    }

    public void setWatchlistName(String watchlistName) {
        this.watchlistName = watchlistName;
    }
}


package com.example.ApiLetter.dto;

import java.time.LocalDateTime;
import java.util.List;

public class WatchlistResponseDTO {
    private Long id;
    private String name;
    private Long userId;
    private String username;
    private List<WatchlistItemDTO> movies;
    private int movieCount;
    private LocalDateTime lastUpdate;
    private Boolean active;

    public WatchlistResponseDTO() {}

    public WatchlistResponseDTO(Long id, String name, Long userId, String username, List<WatchlistItemDTO> movies) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.username = username;
        this.movies = movies;
        this.movieCount = movies != null ? movies.size() : 0;
    }

    public WatchlistResponseDTO(Long id, String name, Long userId, String username, List<WatchlistItemDTO> movies, LocalDateTime lastUpdate, Boolean active) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.username = username;
        this.movies = movies;
        this.movieCount = movies != null ? movies.size() : 0;
        this.lastUpdate = lastUpdate;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<WatchlistItemDTO> getMovies() {
        return movies;
    }

    public void setMovies(List<WatchlistItemDTO> movies) {
        this.movies = movies;
        this.movieCount = movies != null ? movies.size() : 0;
    }

    public int getMovieCount() {
        return movieCount;
    }

    public void setMovieCount(int movieCount) {
        this.movieCount = movieCount;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}


package com.example.ApiLetter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AddMovieToWatchlistDTO {
    
    @NotNull(message = "O watchlistId é obrigatório")
    private Long watchlistId;
    
    @NotBlank(message = "O imdbId é obrigatório")
    private String imdbId;
    
    @NotNull(message = "O userId é obrigatório")
    private Long userId;

    public AddMovieToWatchlistDTO() {}

    public AddMovieToWatchlistDTO(Long watchlistId, String imdbId, Long userId) {
        this.watchlistId = watchlistId;
        this.imdbId = imdbId;
        this.userId = userId;
    }

    public Long getWatchlistId() {
        return watchlistId;
    }

    public void setWatchlistId(Long watchlistId) {
        this.watchlistId = watchlistId;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}


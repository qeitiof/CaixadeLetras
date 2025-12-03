package com.example.ApiLetter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class WatchlistCreateDTO {
    
    @NotBlank(message = "O nome da watchlist é obrigatório")
    private String name;
    
    @NotNull(message = "O userId é obrigatório")
    private Long userId;

    public WatchlistCreateDTO() {}

    public WatchlistCreateDTO(String name, Long userId) {
        this.name = name;
        this.userId = userId;
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
}


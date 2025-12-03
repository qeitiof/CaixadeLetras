package com.example.ApiLetter.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReviewCreateDTO {
    
    @NotBlank(message = "O imdbId é obrigatório")
    private String imdbId;
    
    private String comentario;
    
    @Min(value = 1, message = "A nota deve ser no mínimo 1")
    @Max(value = 5, message = "A nota deve ser no máximo 5")
    @NotNull(message = "A nota é obrigatória")
    private Integer nota; // 1 a 5
    
    @NotNull(message = "O userId é obrigatório")
    private Long userId;

    public ReviewCreateDTO() {}

    public ReviewCreateDTO(String imdbId, String comentario, Integer nota, Long userId) {
        this.imdbId = imdbId;
        this.comentario = comentario;
        this.nota = nota;
        this.userId = userId;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}


package com.example.ApiLetter.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ReviewUpdateDTO {
    
    @Min(value = 1, message = "A nota deve ser no mínimo 1")
    @Max(value = 5, message = "A nota deve ser no máximo 5")
    @NotNull(message = "A nota é obrigatória")
    private Integer nota;
    
    private String comentario;

    public ReviewUpdateDTO() {}

    public ReviewUpdateDTO(Integer nota, String comentario) {
        this.nota = nota;
        this.comentario = comentario;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}


package com.example.ApiLetter.dto;

public class ReviewCreateDTO {
    private String imdbId;
    private String comentario;
    private int nota; // 0 a 5
    private Long userId;

    public ReviewCreateDTO() {}

    public ReviewCreateDTO(String imdbId, String comentario, int nota, Long userId) {
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

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}


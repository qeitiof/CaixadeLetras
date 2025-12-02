package com.example.ApiLetter.dto;

public class ReviewResponseDTO {
    private Long id;
    private String imdbId;
    private String comentario;
    private int nota;
    private String username;
    private String movieTitle;

    public ReviewResponseDTO() {}

    public ReviewResponseDTO(Long id, String imdbId, String comentario, int nota, String username, String movieTitle) {
        this.id = id;
        this.imdbId = imdbId;
        this.comentario = comentario;
        this.nota = nota;
        this.username = username;
        this.movieTitle = movieTitle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }
}


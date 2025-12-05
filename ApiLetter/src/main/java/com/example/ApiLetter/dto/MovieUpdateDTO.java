package com.example.ApiLetter.dto;

import jakarta.validation.constraints.Size;

public class MovieUpdateDTO {
    
    @Size(max = 255, message = "O título deve ter no máximo 255 caracteres")
    private String titulo;
    
    private String imdbId;
    
    private String year;
    
    private String poster;

    public MovieUpdateDTO() {}

    public MovieUpdateDTO(String titulo, String imdbId, String year, String poster) {
        this.titulo = titulo;
        this.imdbId = imdbId;
        this.year = year;
        this.poster = poster;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}


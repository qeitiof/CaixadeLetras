package com.example.ApiLetter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MovieDTO {

    private Long id;

    @JsonProperty("imdbID")
    private String imdbId;

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Year")
    private String year;

    @JsonProperty("Genre")
    private String genre;

    @JsonProperty("Poster")
    private String poster;

    public MovieDTO() {}

    public MovieDTO(Long id, String imdbId, String title, String year, String genre, String poster) {
        this.id = id;
        this.imdbId = imdbId;
        this.title = title;
        this.year = year;
        this.genre = genre;
        this.poster = poster;
    }

    public Long getId() { return id; }
    public String getImdbId() { return imdbId; }
    public String getTitle() { return title; }
    public String getYear() { return year; }
    public String getGenre() { return genre; }
    public String getPoster() { return poster; }

    public void setId(Long id) { this.id = id; }
    public void setImdbId(String imdbId) { this.imdbId = imdbId; }
    public void setTitle(String title) { this.title = title; }
    public void setYear(String year) { this.year = year; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setPoster(String poster) { this.poster = poster; }
}

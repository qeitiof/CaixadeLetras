package com.example.ApiLetter.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String imdbId;
    
    @Column(name = "movie_year")
    private String year;
    
    private String poster;

    @OneToMany(mappedBy = "movie")
    private List<Review> reviews;

    public Movie() {}

    public Movie(String titulo, String imdbId) {
        this.titulo = titulo;
        this.imdbId = imdbId;
    }

    public Movie(String titulo, String imdbId, String year, String poster) {
        this.titulo = titulo;
        this.imdbId = imdbId;
        this.year = year;
        this.poster = poster;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getImdbId() { return imdbId; }
    public void setImdbId(String imdbId) { this.imdbId = imdbId; }

    public List<Review> getReviews() { return reviews; }
    public void setReviews(List<Review> reviews) { this.reviews = reviews; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public String getPoster() { return poster; }
    public void setPoster(String poster) { this.poster = poster; }
}

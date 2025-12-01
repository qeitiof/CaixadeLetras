package com.example.ApiLetter.model;

import jakarta.persistence.*;

@Entity
@Table(name = "watchlist_changes")
public class WatchlistChange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action; // ADD ou REMOVE

    @ManyToOne
    @JoinColumn(name = "watchlist_id")
    private Watchlist watchlist;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    public WatchlistChange() {}

    public WatchlistChange(String action, Watchlist watchlist, Movie movie) {
        this.action = action;
        this.watchlist = watchlist;
        this.movie = movie;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public Watchlist getWatchlist() { return watchlist; }
    public void setWatchlist(Watchlist watchlist) { this.watchlist = watchlist; }

    public Movie getMovie() { return movie; }
    public void setMovie(Movie movie) { this.movie = movie; }
}

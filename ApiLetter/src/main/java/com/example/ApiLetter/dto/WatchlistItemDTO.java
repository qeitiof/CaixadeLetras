package com.example.ApiLetter.dto;

public class WatchlistItemDTO {
    private Long id;
    private String imdbId;
    private String title;
    private String year;
    private String poster;

    public WatchlistItemDTO() {}

    public WatchlistItemDTO(Long id, String imdbId, String title, String year, String poster) {
        this.id = id;
        this.imdbId = imdbId;
        this.title = title;
        this.year = year;
        this.poster = poster;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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


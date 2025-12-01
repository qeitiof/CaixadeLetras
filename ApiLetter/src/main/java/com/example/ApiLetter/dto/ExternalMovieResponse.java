package com.example.ApiLetter.dto;

import java.util.List;

public class ExternalMovieResponse {

    private String imdbid;
    private String title;
    private String year;
    private List<String> genres;
    private String thumbnail;

    public String getImdbid() { return imdbid; }
    public void setImdbid(String imdbid) { this.imdbid = imdbid; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public List<String> getGenres() { return genres; }
    public void setGenres(List<String> genres) { this.genres = genres; }

    public String getThumbnail() { return thumbnail; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }
}

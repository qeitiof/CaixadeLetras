package com.example.ApiLetter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class OmdbSearchResponse {
    
    @JsonProperty("Search")
    private List<OmdbMovieItem> search;
    
    @JsonProperty("totalResults")
    private String totalResults;
    
    @JsonProperty("Response")
    private String response;
    
    public List<OmdbMovieItem> getSearch() {
        return search;
    }
    
    public void setSearch(List<OmdbMovieItem> search) {
        this.search = search;
    }
    
    public String getTotalResults() {
        return totalResults;
    }
    
    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }
    
    public String getResponse() {
        return response;
    }
    
    public void setResponse(String response) {
        this.response = response;
    }
    
    public static class OmdbMovieItem {
        @JsonProperty("Title")
        private String title;
        
        @JsonProperty("Year")
        private String year;
        
        @JsonProperty("imdbID")
        private String imdbId;
        
        @JsonProperty("Type")
        private String type;
        
        @JsonProperty("Poster")
        private String poster;
        
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
        
        public String getImdbId() {
            return imdbId;
        }
        
        public void setImdbId(String imdbId) {
            this.imdbId = imdbId;
        }
        
        public String getType() {
            return type;
        }
        
        public void setType(String type) {
            this.type = type;
        }
        
        public String getPoster() {
            return poster;
        }
        
        public void setPoster(String poster) {
            this.poster = poster;
        }
    }
}


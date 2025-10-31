package com.example.caixadeletras.service;

import com.example.caixadeletras.model.Filme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FilmeService {

    @Value("${omdb.api.key}")
    private String omdbApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public Filme buscarFilmePorTitulo(String titulo) {
        String url = "http://www.omdbapi.com/?apikey=" + omdbApiKey + "&t=" + titulo.replace(" ", "+");
        return restTemplate.getForObject(url, Filme.class);
    }
}

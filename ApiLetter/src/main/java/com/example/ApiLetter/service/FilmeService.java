package com.example.ApiLetter.service;

import com.example.ApiLetter.dto.MovieDTO;
import com.example.ApiLetter.model.Movie;
import com.example.ApiLetter.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FilmeService {

    @Value("${omdb.api.key}")
    private String omdbApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final MovieRepository movieRepository;

    public FilmeService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public MovieDTO buscarFilmePorTitulo(String titulo) {

        String url = "http://www.omdbapi.com/?apikey=" + omdbApiKey + "&t=" + titulo.replace(" ", "+");

        MovieDTO dto = restTemplate.getForObject(url, MovieDTO.class);

        // Se não achar no OMDB, retorna null para o controller
        if (dto == null || dto.getImdbId() == null) {
            return null;
        }

        // Verificar se já existe no banco
        Movie existente = movieRepository.findByImdbId(dto.getImdbId());

        // Se não existe → cria e salva
        if (existente == null) {
            Movie novo = new Movie(dto.getTitle(), dto.getImdbId());
            movieRepository.save(novo);
        }

        return dto;
    }
}

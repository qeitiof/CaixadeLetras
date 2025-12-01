package com.example.ApiLetter.controller;

import com.example.ApiLetter.dto.MovieDTO;
import com.example.ApiLetter.service.FilmeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/movies")
public class MovieController {

    @Value("${omdb.api.key}")
    private String omdbApiKey;

    private final FilmeService filmeService;

    public MovieController(FilmeService filmeService) {
        this.filmeService = filmeService;
    }

    // ================================
    // BUSCAR FILME EXATO PELO TÍTULO
    // ================================
    @GetMapping("/search")
    public ResponseEntity<MovieDTO> buscarFilme(@RequestParam String titulo) {
        MovieDTO dto = filmeService.buscarFilmePorTitulo(titulo);

        if (dto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(dto);
    }

    // ================================
    // SUGESTÕES (MATCHES)
    // ================================
    @GetMapping("/suggest")
    public ResponseEntity<?> sugerir(@RequestParam String titulo) {
        try {
            String url = "https://www.omdbapi.com/?apikey=" + omdbApiKey + "&s=" + titulo;

            RestTemplate rt = new RestTemplate();
            String resposta = rt.getForObject(url, String.class);

            return ResponseEntity.ok(resposta);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao buscar sugestões");
        }
    }
}

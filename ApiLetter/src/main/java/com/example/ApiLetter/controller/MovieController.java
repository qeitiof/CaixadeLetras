package com.example.ApiLetter.controller;

import com.example.ApiLetter.dto.MovieDTO;
import com.example.ApiLetter.service.FilmeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/movies")
public class MovieController {

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
    // SUGESTÕES (MATCHES) COM FALLBACK
    // ================================
    @GetMapping("/suggest")
    public ResponseEntity<Map<String, Object>> sugerir(@RequestParam String titulo) {
        try {
            Map<String, Object> resultado = filmeService.buscarSugestoes(titulo);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            System.err.println("Erro no controller ao buscar sugestões: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> erro = new java.util.HashMap<>();
            erro.put("Response", "False");
            erro.put("Error", "Erro ao buscar sugestões: " + e.getMessage());
            erro.put("Search", new java.util.ArrayList<>());
            return ResponseEntity.status(500).body(erro);
        }
    }
}

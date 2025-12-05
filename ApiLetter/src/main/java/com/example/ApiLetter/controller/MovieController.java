package com.example.ApiLetter.controller;

import com.example.ApiLetter.dto.MovieDTO;
import com.example.ApiLetter.dto.MovieCreateDTO;
import com.example.ApiLetter.dto.MovieUpdateDTO;
import com.example.ApiLetter.model.Movie;
import com.example.ApiLetter.service.FilmeService;
import com.example.ApiLetter.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/movies")
public class MovieController {

    private final FilmeService filmeService;
    private final MovieService movieService;

    public MovieController(FilmeService filmeService, MovieService movieService) {
        this.filmeService = filmeService;
        this.movieService = movieService;
    }

    // ================================
    // CRUD COMPLETO
    // ================================
    
    // GET ALL com paginação, ordenação e filtros
    @GetMapping
    public ResponseEntity<Page<Movie>> listarTodos(
            @PageableDefault(size = 10, sort = "titulo") Pageable pageable,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String imdbId) {
        Page<Movie> movies = movieService.listarTodos(pageable, titulo, year, imdbId);
        return ResponseEntity.ok(movies);
    }

    // GET ONE
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Movie movie = movieService.buscarPorId(id);
        return ResponseEntity.ok(movie);
    }

    // CREATE
    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody MovieCreateDTO dto) {
        Movie movie = new Movie();
        movie.setTitulo(dto.getTitulo());
        movie.setImdbId(dto.getImdbId());
        movie.setYear(dto.getYear());
        movie.setPoster(dto.getPoster());
        Movie novo = movieService.criar(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(novo);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody MovieUpdateDTO dto) {
        Movie movie = new Movie();
        movie.setTitulo(dto.getTitulo());
        movie.setImdbId(dto.getImdbId());
        movie.setYear(dto.getYear());
        movie.setPoster(dto.getPoster());
        Movie atualizado = movieService.atualizar(id, movie);
        return ResponseEntity.ok(atualizado);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        movieService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // ================================
    // BUSCAR FILME EXATO PELO TÍTULO (API Externa)
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

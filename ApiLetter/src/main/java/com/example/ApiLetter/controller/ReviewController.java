package com.example.ApiLetter.controller;

import com.example.ApiLetter.dto.ReviewCreateDTO;
import com.example.ApiLetter.dto.ReviewResponseDTO;
import com.example.ApiLetter.dto.ReviewUpdateDTO;
import com.example.ApiLetter.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<?> criarAvaliacao(@Valid @RequestBody ReviewCreateDTO dto) {
        ReviewResponseDTO response = reviewService.criarAvaliacao(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET ALL com paginação, ordenação e filtros
    @GetMapping
    public ResponseEntity<Page<ReviewResponseDTO>> listarTodos(
            @PageableDefault(size = 10, sort = "id") Pageable pageable,
            @RequestParam(required = false) Integer notaMin,
            @RequestParam(required = false) Integer notaMax,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String imdbId) {
        Page<ReviewResponseDTO> reviews = reviewService.listarTodos(pageable, notaMin, notaMax, userId, imdbId);
        return ResponseEntity.ok(reviews);
    }

    // GET ONE
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        ReviewResponseDTO review = reviewService.buscarPorId(id);
        return ResponseEntity.ok(review);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody ReviewUpdateDTO dto) {
        ReviewResponseDTO response = reviewService.atualizar(id, dto);
        return ResponseEntity.ok(response);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        reviewService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoints específicos (mantidos para compatibilidade)
    @GetMapping("/movie/{imdbId}")
    public ResponseEntity<List<ReviewResponseDTO>> buscarAvaliacoesPorFilme(@PathVariable String imdbId) {
        List<ReviewResponseDTO> reviews = reviewService.buscarAvaliacoesPorFilme(imdbId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponseDTO>> buscarAvaliacoesPorUsuario(@PathVariable Long userId) {
        List<ReviewResponseDTO> reviews = reviewService.buscarAvaliacoesPorUsuario(userId);
        return ResponseEntity.ok(reviews);
    }
}


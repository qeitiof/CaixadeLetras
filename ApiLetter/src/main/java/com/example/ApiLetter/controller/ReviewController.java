package com.example.ApiLetter.controller;

import com.example.ApiLetter.dto.ReviewCreateDTO;
import com.example.ApiLetter.dto.ReviewResponseDTO;
import com.example.ApiLetter.service.ReviewService;
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

    @PostMapping
    public ResponseEntity<ReviewResponseDTO> criarAvaliacao(@RequestBody ReviewCreateDTO dto) {
        try {
            ReviewResponseDTO response = reviewService.criarAvaliacao(dto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).build();
        }
    }

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


package com.example.ApiLetter.service;

import com.example.ApiLetter.dto.ReviewCreateDTO;
import com.example.ApiLetter.dto.ReviewResponseDTO;
import com.example.ApiLetter.model.Movie;
import com.example.ApiLetter.model.Review;
import com.example.ApiLetter.model.User;
import com.example.ApiLetter.repository.MovieRepository;
import com.example.ApiLetter.repository.ReviewRepository;
import com.example.ApiLetter.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public ReviewService(ReviewRepository reviewRepository, 
                        UserRepository userRepository,
                        MovieRepository movieRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    public ReviewResponseDTO criarAvaliacao(ReviewCreateDTO dto) {
        // Validar nota (1 a 5)
        if (dto.getNota() < 1 || dto.getNota() > 5) {
            throw new IllegalArgumentException("A nota deve estar entre 1 e 5");
        }

        // Buscar usuário
        User user = userRepository.findById(dto.getUserId())
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Buscar ou criar filme
        Movie movie = movieRepository.findByImdbId(dto.getImdbId());
        if (movie == null) {
            // Se o filme não existe, criar um básico
            movie = new Movie("", dto.getImdbId());
            movie = movieRepository.save(movie);
        }

        // Verificar se o usuário já avaliou este filme
        Review reviewExistente = reviewRepository.findByUserIdAndImdbId(dto.getUserId(), dto.getImdbId());
        
        if (reviewExistente != null) {
            // Atualizar avaliação existente
            reviewExistente.setNota(dto.getNota());
            reviewExistente.setComentario(dto.getComentario());
            reviewRepository.save(reviewExistente);
            
            return toResponseDTO(reviewExistente);
        } else {
            // Criar nova avaliação
            Review review = new Review(dto.getImdbId(), dto.getComentario(), dto.getNota(), user, movie);
            review = reviewRepository.save(review);
            
            return toResponseDTO(review);
        }
    }

    public List<ReviewResponseDTO> buscarAvaliacoesPorFilme(String imdbId) {
        List<Review> reviews = reviewRepository.findByImdbId(imdbId);
        return reviews.stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    public List<ReviewResponseDTO> buscarAvaliacoesPorUsuario(Long userId) {
        List<Review> reviews = reviewRepository.findByUserId(userId);
        return reviews.stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    private ReviewResponseDTO toResponseDTO(Review review) {
        String movieTitle = "";
        String moviePoster = "";
        
        if (review.getMovie() != null) {
            movieTitle = review.getMovie().getTitulo() != null ? review.getMovie().getTitulo() : "";
            moviePoster = review.getMovie().getPoster() != null ? review.getMovie().getPoster() : "";
        }
        
        return new ReviewResponseDTO(
            review.getId(),
            review.getImdbId(),
            review.getComentario(),
            review.getNota(),
            review.getUser().getUsername(),
            movieTitle,
            moviePoster
        );
    }
}


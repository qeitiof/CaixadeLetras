package com.example.ApiLetter.service;

import com.example.ApiLetter.dto.ReviewCreateDTO;
import com.example.ApiLetter.dto.ReviewResponseDTO;
import com.example.ApiLetter.dto.ReviewUpdateDTO;
import com.example.ApiLetter.model.Movie;
import com.example.ApiLetter.model.Review;
import com.example.ApiLetter.model.User;
import com.example.ApiLetter.repository.MovieRepository;
import com.example.ApiLetter.repository.ReviewRepository;
import com.example.ApiLetter.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
        // Validação já feita pelo @Valid no controller

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

    // GET ALL com paginação, ordenação e filtros
    public Page<ReviewResponseDTO> listarTodos(Pageable pageable, Integer notaMin, Integer notaMax, Long userId, String imdbId) {
        Specification<Review> spec = Specification.where(null);
        
        if (notaMin != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("nota"), notaMin));
        }
        if (notaMax != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("nota"), notaMax));
        }
        if (userId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("user").get("id"), userId));
        }
        if (imdbId != null && !imdbId.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("imdbId"), imdbId));
        }
        
        Page<Review> reviews = reviewRepository.findAll(spec, pageable);
        return reviews.map(this::toResponseDTO);
    }

    // GET ONE
    public ReviewResponseDTO buscarPorId(Long id) {
        Review review = reviewRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));
        return toResponseDTO(review);
    }

    // UPDATE
    public ReviewResponseDTO atualizar(Long id, ReviewUpdateDTO dto) {
        Review review = reviewRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));
        
        if (dto.getNota() != null) {
            review.setNota(dto.getNota());
        }
        if (dto.getComentario() != null) {
            review.setComentario(dto.getComentario());
        }
        
        review = reviewRepository.save(review);
        return toResponseDTO(review);
    }

    // DELETE
    public void deletar(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new RuntimeException("Avaliação não encontrada");
        }
        reviewRepository.deleteById(id);
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


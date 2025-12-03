package com.example.ApiLetter.service;

import com.example.ApiLetter.dto.MovieDTO;
import com.example.ApiLetter.model.Movie;
import com.example.ApiLetter.repository.MovieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final FilmeService filmeService;

    public MovieService(MovieRepository movieRepository, FilmeService filmeService) {
        this.movieRepository = movieRepository;
        this.filmeService = filmeService;
    }

    // GET ALL com paginação, ordenação e filtros
    public Page<Movie> listarTodos(Pageable pageable, String titulo, String year, String imdbId) {
        Specification<Movie> spec = Specification.where(null);
        
        if (titulo != null && !titulo.isEmpty()) {
            spec = spec.and((root, query, cb) -> 
                cb.like(cb.lower(root.get("titulo")), "%" + titulo.toLowerCase() + "%"));
        }
        if (year != null && !year.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("year"), year));
        }
        if (imdbId != null && !imdbId.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("imdbId"), imdbId));
        }
        
        return movieRepository.findAll(spec, pageable);
    }

    // GET ONE
    public Movie buscarPorId(Long id) {
        return movieRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Filme não encontrado"));
    }

    // CREATE
    public Movie criar(Movie movie) {
        // Verificar se já existe pelo imdbId
        if (movie.getImdbId() != null) {
            Movie existente = movieRepository.findByImdbId(movie.getImdbId());
            if (existente != null) {
                throw new RuntimeException("Filme com este imdbId já existe");
            }
        }
        return movieRepository.save(movie);
    }

    // UPDATE
    public Movie atualizar(Long id, Movie movieAtualizado) {
        Movie movie = movieRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Filme não encontrado"));
        
        if (movieAtualizado.getTitulo() != null) {
            movie.setTitulo(movieAtualizado.getTitulo());
        }
        if (movieAtualizado.getYear() != null) {
            movie.setYear(movieAtualizado.getYear());
        }
        if (movieAtualizado.getPoster() != null) {
            movie.setPoster(movieAtualizado.getPoster());
        }
        if (movieAtualizado.getImdbId() != null) {
            // Verificar se o novo imdbId já existe em outro filme
            Movie existente = movieRepository.findByImdbId(movieAtualizado.getImdbId());
            if (existente != null && !existente.getId().equals(id)) {
                throw new RuntimeException("Filme com este imdbId já existe");
            }
            movie.setImdbId(movieAtualizado.getImdbId());
        }
        
        return movieRepository.save(movie);
    }

    // DELETE
    public void deletar(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new RuntimeException("Filme não encontrado");
        }
        movieRepository.deleteById(id);
    }
}


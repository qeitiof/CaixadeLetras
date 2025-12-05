package com.example.ApiLetter.service;

import com.example.ApiLetter.dto.*;
import com.example.ApiLetter.model.*;
import com.example.ApiLetter.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Service
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final WatchlistChangeRepository watchlistChangeRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final FilmeService filmeService;

    public WatchlistService(WatchlistRepository watchlistRepository,
                           WatchlistChangeRepository watchlistChangeRepository,
                           UserRepository userRepository,
                           MovieRepository movieRepository,
                           FilmeService filmeService) {
        this.watchlistRepository = watchlistRepository;
        this.watchlistChangeRepository = watchlistChangeRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.filmeService = filmeService;
    }

    public WatchlistResponseDTO criarWatchlist(WatchlistCreateDTO dto) {
        User user = userRepository.findById(dto.getUserId())
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Watchlist watchlist = new Watchlist(dto.getName(), user);
        watchlist.setLastUpdate(LocalDateTime.now());
        watchlist.setActive(true);
        watchlist = watchlistRepository.save(watchlist);

        return toResponseDTO(watchlist);
    }

    @Transactional
    public WatchlistResponseDTO adicionarFilme(AddMovieToWatchlistDTO dto) {
        Watchlist watchlist = watchlistRepository.findById(dto.getWatchlistId())
            .orElseThrow(() -> new RuntimeException("Watchlist não encontrada"));

        // Verificar se o usuário é o dono da watchlist
        if (!watchlist.getUser().getId().equals(dto.getUserId())) {
            throw new RuntimeException("Você não tem permissão para modificar esta watchlist");
        }

        // Buscar ou criar filme
        Movie movie = movieRepository.findByImdbId(dto.getImdbId());
        if (movie == null) {
            // Se não existe no banco, criar um básico
            // O filme será atualizado quando for avaliado ou quando buscado via API
            movie = new Movie("", dto.getImdbId());
            movie = movieRepository.save(movie);
        }

        // Criar variável final para usar na lambda
        final Movie finalMovie = movie;
        final Long movieId = movie.getId();

        // Verificar se o filme já está na watchlist (última ação foi ADD)
        List<WatchlistChange> changes = watchlistChangeRepository.findByWatchlistId(watchlist.getId());
        boolean jaAdicionado = changes.stream()
            .filter(c -> c.getMovie().getId().equals(movieId))
            .anyMatch(c -> {
                // Verificar se a última ação para este filme foi ADD
                List<WatchlistChange> filmeChanges = changes.stream()
                    .filter(ch -> ch.getMovie().getId().equals(movieId))
                    .sorted((a, b) -> Long.compare(b.getId(), a.getId()))
                    .collect(Collectors.toList());
                return !filmeChanges.isEmpty() && "ADD".equals(filmeChanges.get(0).getAction());
            });

        if (jaAdicionado) {
            throw new RuntimeException("Este filme já está na watchlist");
        }

        // Adicionar filme
        WatchlistChange change = new WatchlistChange("ADD", watchlist, finalMovie);
        watchlistChangeRepository.save(change);
        
        // Atualizar última atualização
        watchlist.setLastUpdate(LocalDateTime.now());
        watchlistRepository.save(watchlist);

        return toResponseDTO(watchlist);
    }

    @Transactional
    public WatchlistResponseDTO removerFilme(Long watchlistId, Long movieId, Long userId) {
        Watchlist watchlist = watchlistRepository.findById(watchlistId)
            .orElseThrow(() -> new RuntimeException("Watchlist não encontrada"));

        // Verificar se o usuário é o dono da watchlist
        if (!watchlist.getUser().getId().equals(userId)) {
            throw new RuntimeException("Você não tem permissão para modificar esta watchlist");
        }

        Movie movie = movieRepository.findById(movieId)
            .orElseThrow(() -> new RuntimeException("Filme não encontrado"));

        // Verificar se o filme está na watchlist
        List<WatchlistChange> changes = watchlistChangeRepository.findByWatchlistId(watchlistId);
        boolean estaNaWatchlist = changes.stream()
            .filter(c -> c.getMovie().getId().equals(movieId))
            .anyMatch(c -> {
                List<WatchlistChange> filmeChanges = changes.stream()
                    .filter(ch -> ch.getMovie().getId().equals(movieId))
                    .sorted((a, b) -> Long.compare(b.getId(), a.getId()))
                    .collect(Collectors.toList());
                return !filmeChanges.isEmpty() && "ADD".equals(filmeChanges.get(0).getAction());
            });

        if (!estaNaWatchlist) {
            throw new RuntimeException("Este filme não está na watchlist");
        }

        // Remover filme
        WatchlistChange change = new WatchlistChange("REMOVE", watchlist, movie);
        watchlistChangeRepository.save(change);
        
        // Atualizar última atualização
        watchlist.setLastUpdate(LocalDateTime.now());
        watchlistRepository.save(watchlist);

        return toResponseDTO(watchlist);
    }

    public List<WatchlistResponseDTO> listarWatchlistsDoUsuario(Long userId) {
        List<Watchlist> watchlists = watchlistRepository.findByUserId(userId);
        return watchlists.stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    // GET ALL com paginação, ordenação e filtros
    public Page<WatchlistResponseDTO> listarTodos(Pageable pageable, Long userId, String name, Boolean active) {
        Specification<Watchlist> spec = Specification.where(null);
        
        if (userId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("user").get("id"), userId));
        }
        if (name != null && !name.isEmpty()) {
            spec = spec.and((root, query, cb) -> 
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }
        if (active != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("active"), active));
        }
        
        Page<Watchlist> page = watchlistRepository.findAll(spec, pageable);
        return page.map(this::toResponseDTO);
    }

    public List<WatchlistResponseDTO> listarWatchlistsDeOutroUsuario(Long userId) {
        List<Watchlist> watchlists = watchlistRepository.findByUserId(userId);
        return watchlists.stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    public WatchlistResponseDTO buscarWatchlistPorId(Long watchlistId) {
        Watchlist watchlist = watchlistRepository.findById(watchlistId)
            .orElseThrow(() -> new RuntimeException("Watchlist não encontrada"));
        return toResponseDTO(watchlist);
    }

    @Transactional
    public void deletarWatchlist(Long watchlistId, Long userId) {
        Watchlist watchlist = watchlistRepository.findById(watchlistId)
            .orElseThrow(() -> new RuntimeException("Watchlist não encontrada"));

        if (!watchlist.getUser().getId().equals(userId)) {
            throw new RuntimeException("Você não tem permissão para deletar esta watchlist");
        }

        watchlistRepository.delete(watchlist);
    }

    // Listar watchlists inativas por mais de uma semana
    public List<WatchlistResponseDTO> listarInativasPorMaisDeUmaSemana() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        List<Watchlist> watchlists = watchlistRepository.findInactiveForMoreThanOneWeek(oneWeekAgo);
        return watchlists.stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    // Inativar watchlist
    @Transactional
    public WatchlistResponseDTO inativarWatchlist(Long watchlistId, Long userId) {
        Watchlist watchlist = watchlistRepository.findById(watchlistId)
            .orElseThrow(() -> new RuntimeException("Watchlist não encontrada"));

        if (!watchlist.getUser().getId().equals(userId)) {
            throw new RuntimeException("Você não tem permissão para modificar esta watchlist");
        }

        watchlist.setActive(false);
        watchlist.setLastUpdate(LocalDateTime.now());
        watchlist = watchlistRepository.save(watchlist);

        return toResponseDTO(watchlist);
    }

    // Ativar watchlist
    @Transactional
    public WatchlistResponseDTO ativarWatchlist(Long watchlistId, Long userId) {
        Watchlist watchlist = watchlistRepository.findById(watchlistId)
            .orElseThrow(() -> new RuntimeException("Watchlist não encontrada"));

        if (!watchlist.getUser().getId().equals(userId)) {
            throw new RuntimeException("Você não tem permissão para modificar esta watchlist");
        }

        watchlist.setActive(true);
        watchlist.setLastUpdate(LocalDateTime.now());
        watchlist = watchlistRepository.save(watchlist);

        return toResponseDTO(watchlist);
    }

    // Buscar histórico de mudanças de uma watchlist
    public List<WatchlistHistoryDTO> buscarHistorico(Long watchlistId) {
        Watchlist watchlist = watchlistRepository.findById(watchlistId)
            .orElseThrow(() -> new RuntimeException("Watchlist não encontrada"));

        List<WatchlistChange> changes = watchlistChangeRepository.findByWatchlistIdOrderByCreatedAtDesc(watchlistId);
        
        return changes.stream()
            .map(change -> {
                Movie movie = change.getMovie();
                WatchlistItemDTO movieDTO = new WatchlistItemDTO(
                    movie.getId(),
                    movie.getImdbId(),
                    movie.getTitulo() != null ? movie.getTitulo() : "",
                    movie.getYear() != null ? movie.getYear() : "",
                    movie.getPoster() != null ? movie.getPoster() : ""
                );
                
                return new WatchlistHistoryDTO(
                    change.getId(),
                    change.getAction(),
                    change.getCreatedAt() != null ? change.getCreatedAt() : LocalDateTime.now(),
                    movieDTO,
                    watchlist.getName()
                );
            })
            .collect(Collectors.toList());
    }

    private WatchlistResponseDTO toResponseDTO(Watchlist watchlist) {
        List<WatchlistChange> allChanges = watchlistChangeRepository.findByWatchlistId(watchlist.getId());
        
        // Filtrar apenas filmes que estão atualmente na watchlist (última ação foi ADD)
        List<WatchlistItemDTO> movies = new ArrayList<>();
        
        // Agrupar por filme e pegar a última ação
        allChanges.stream()
            .collect(Collectors.groupingBy(WatchlistChange::getMovie))
            .forEach((movie, changes) -> {
                // Ordenar por ID (mais recente primeiro)
                changes.sort((a, b) -> Long.compare(b.getId(), a.getId()));
                WatchlistChange ultimaAcao = changes.get(0);
                
                if ("ADD".equals(ultimaAcao.getAction())) {
                    Movie m = ultimaAcao.getMovie();
                    movies.add(new WatchlistItemDTO(
                        m.getId(),
                        m.getImdbId(),
                        m.getTitulo() != null ? m.getTitulo() : "",
                        m.getYear() != null ? m.getYear() : "",
                        m.getPoster() != null ? m.getPoster() : ""
                    ));
                }
            });

        WatchlistResponseDTO dto = new WatchlistResponseDTO(
            watchlist.getId(),
            watchlist.getName(),
            watchlist.getUser().getId(),
            watchlist.getUser().getUsername(),
            movies,
            watchlist.getLastUpdate(),
            watchlist.getActive()
        );
        return dto;
    }
}


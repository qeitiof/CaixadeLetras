package com.example.ApiLetter.controller;

import com.example.ApiLetter.dto.*;
import com.example.ApiLetter.service.WatchlistService;
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
@RequestMapping("/watchlists")
public class WatchlistController {

    private final WatchlistService watchlistService;

    public WatchlistController(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    // Criar nova watchlist
    @PostMapping
    public ResponseEntity<?> criarWatchlist(@Valid @RequestBody WatchlistCreateDTO dto) {
        try {
            WatchlistResponseDTO response = watchlistService.criarWatchlist(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Adicionar filme à watchlist
    @PostMapping("/add-movie")
    public ResponseEntity<?> adicionarFilme(@Valid @RequestBody AddMovieToWatchlistDTO dto) {
        try {
            WatchlistResponseDTO response = watchlistService.adicionarFilme(dto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Remover filme da watchlist
    @DeleteMapping("/{watchlistId}/movies/{movieId}")
    public ResponseEntity<?> removerFilme(
            @PathVariable Long watchlistId,
            @PathVariable Long movieId,
            @RequestParam Long userId) {
        try {
            WatchlistResponseDTO response = watchlistService.removerFilme(watchlistId, movieId, userId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // GET ALL com paginação, ordenação e filtros
    @GetMapping
    public ResponseEntity<Page<WatchlistResponseDTO>> listarTodos(
            @PageableDefault(size = 10, sort = "id") Pageable pageable,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean active) {
        Page<WatchlistResponseDTO> watchlists = watchlistService.listarTodos(pageable, userId, name, active);
        return ResponseEntity.ok(watchlists);
    }

    // Listar watchlists do usuário logado (mantido para compatibilidade)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WatchlistResponseDTO>> listarWatchlistsDoUsuario(@PathVariable Long userId) {
        List<WatchlistResponseDTO> watchlists = watchlistService.listarWatchlistsDoUsuario(userId);
        return ResponseEntity.ok(watchlists);
    }

    // Buscar watchlist por ID (para visualizar watchlist de outro usuário)
    @GetMapping("/{watchlistId}")
    public ResponseEntity<?> buscarWatchlistPorId(@PathVariable Long watchlistId) {
        try {
            WatchlistResponseDTO response = watchlistService.buscarWatchlistPorId(watchlistId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Arquivar watchlist (inativa ao invés de deletar)
    @DeleteMapping("/{watchlistId}")
    public ResponseEntity<?> arquivarWatchlist(
            @PathVariable Long watchlistId,
            @RequestParam Long userId) {
        WatchlistResponseDTO response = watchlistService.arquivarWatchlist(watchlistId, userId);
        return ResponseEntity.ok(response);
    }

    // Listar watchlists arquivadas de um usuário
    @GetMapping("/arquivadas/user/{userId}")
    public ResponseEntity<List<WatchlistResponseDTO>> listarArquivadas(@PathVariable Long userId) {
        List<WatchlistResponseDTO> watchlists = watchlistService.listarArquivadas(userId);
        return ResponseEntity.ok(watchlists);
    }

    // Listar watchlists inativas por mais de uma semana
    @GetMapping("/inativos")
    public ResponseEntity<List<WatchlistResponseDTO>> listarInativas() {
        List<WatchlistResponseDTO> watchlists = watchlistService.listarInativasPorMaisDeUmaSemana();
        return ResponseEntity.ok(watchlists);
    }

    // Inativar watchlist (mantido para compatibilidade)
    @PutMapping("/{watchlistId}/inativar")
    public ResponseEntity<?> inativarWatchlist(
            @PathVariable Long watchlistId,
            @RequestParam Long userId) {
        WatchlistResponseDTO response = watchlistService.inativarWatchlist(watchlistId, userId);
        return ResponseEntity.ok(response);
    }

    // Ativar watchlist (reativar uma watchlist arquivada)
    @PutMapping("/{watchlistId}/ativar")
    public ResponseEntity<?> ativarWatchlist(
            @PathVariable Long watchlistId,
            @RequestParam Long userId) {
        WatchlistResponseDTO response = watchlistService.ativarWatchlist(watchlistId, userId);
        return ResponseEntity.ok(response);
    }

    // Buscar histórico de mudanças de uma watchlist
    @GetMapping("/{watchlistId}/historico")
    public ResponseEntity<?> buscarHistorico(@PathVariable Long watchlistId) {
        try {
            List<WatchlistHistoryDTO> historico = watchlistService.buscarHistorico(watchlistId);
            return ResponseEntity.ok(historico);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}


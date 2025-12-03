package com.example.ApiLetter.controller;

import com.example.ApiLetter.dto.UserLoginResponseDTO;
import com.example.ApiLetter.dto.UserCreateDTO;
import com.example.ApiLetter.dto.UserUpdateDTO;
import com.example.ApiLetter.dto.UserLoginDTO;
import com.example.ApiLetter.dto.PageResponseDTO;
import com.example.ApiLetter.model.User;
import com.example.ApiLetter.model.Follow;
import com.example.ApiLetter.service.UserService;
import com.example.ApiLetter.service.FollowService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private FollowService followService;

    // GET ALL com paginação e ordenação
    @GetMapping
    public ResponseEntity<?> getAllUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sort) {
        
        // Se não há parâmetros de paginação, retorna lista simples para compatibilidade com frontend
        if (page == null && size == null && sort == null && 
            (username == null || username.isEmpty()) && 
            (email == null || email.isEmpty())) {
            List<User> users = service.listAll();
            return ResponseEntity.ok(users);
        }
        
        // Se há parâmetros de paginação ou filtros, usa paginação
        Pageable pageable = PageRequest.of(
            page != null ? page : 0,
            size != null ? size : 10,
            sort != null ? Sort.by(sort) : Sort.by("username")
        );
        
        Page<User> pageResult = service.listAll(pageable, username, email);
        PageResponseDTO<User> response = new PageResponseDTO<>(
            pageResult.getContent(),
            pageResult.getTotalPages(),
            pageResult.getTotalElements(),
            pageResult.getSize(),
            pageResult.getNumber(),
            pageResult.isFirst(),
            pageResult.isLast(),
            pageResult.getNumberOfElements()
        );
        return ResponseEntity.ok(response);
    }

    // GET ONE
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        User user = service.findById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }
        return ResponseEntity.ok(user);
    }

    // CREATE
    @PostMapping
    public ResponseEntity<?> create(@RequestBody UserCreateDTO dto) {
        try {
            User user = service.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UserUpdateDTO dto) {
        User user = service.update(id, dto);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }
        return ResponseEntity.ok(user);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }
    }

    // Login com retorno de seguidores e seguindo
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO dto) {
        try {
            UserLoginResponseDTO resposta = service.login(dto);
            return ResponseEntity.ok(resposta);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // =========================
    // SEGUIR UM USUÁRIO
    // =========================
    @PostMapping("/{id}/follow")
    public ResponseEntity<?> seguirUsuario(@PathVariable Long id, @RequestParam Long targetId) {
        try {
            Follow follow = followService.seguir(id, targetId);
            return ResponseEntity.ok(follow);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // =========================
    // LISTAR SEGUIDORES
    // =========================
    @GetMapping("/{id}/followers")
    public ResponseEntity<List<User>> getSeguidores(@PathVariable Long id) {
        List<User> seguidores = followService.meusSeguidores(id);
        return ResponseEntity.ok(seguidores);
    }

    // =========================
    // LISTAR QUEM EU SIGO
    // =========================
    @GetMapping("/{id}/following")
    public ResponseEntity<List<User>> getSeguindo(@PathVariable Long id) {
        List<User> seguindo = followService.pessoasQueSigo(id);
        return ResponseEntity.ok(seguindo);
    }
}

package com.example.ApiLetter.controller;

import com.example.ApiLetter.dto.UserLoginResponseDTO;
import com.example.ApiLetter.dto.UserCreateDTO;
import com.example.ApiLetter.dto.UserUpdateDTO;
import com.example.ApiLetter.dto.UserLoginDTO;
import com.example.ApiLetter.model.User;
import com.example.ApiLetter.model.Follow;
import com.example.ApiLetter.service.UserService;
import com.example.ApiLetter.service.FollowService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping
    public List<User> getAllUsers() {
        return service.listAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public User create(@RequestBody UserCreateDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody UserUpdateDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // Login com retorno de seguidores e seguindo
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO dto) {
        try {
            UserLoginResponseDTO resposta = service.login(dto);
            return ResponseEntity.ok(resposta);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
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
            return ResponseEntity.status(400).body(e.getMessage());
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

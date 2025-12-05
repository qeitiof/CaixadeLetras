package com.example.ApiLetter.controller;

import com.example.ApiLetter.model.Follow;
import com.example.ApiLetter.service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/follows")
@CrossOrigin(origins = "*")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    // Seguir alguém
    @PostMapping("/{id}/follow")
    public ResponseEntity<?> seguirUsuario(@PathVariable("id") Long followerId,
                                           @RequestParam Long followedId) {
        Follow resultado = followService.seguir(followerId, followedId);
        return ResponseEntity.ok(resultado);
    }


    // Lista pessoas que sigo
    @GetMapping("/following/{userId}")
    public ResponseEntity<?> pessoasQueSigo(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.pessoasQueSigo(userId));
    }

    // Lista seguidores
    @GetMapping("/followers/{userId}")
    public ResponseEntity<?> meusSeguidores(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.meusSeguidores(userId));
    }
}

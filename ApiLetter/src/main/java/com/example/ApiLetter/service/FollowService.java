package com.example.ApiLetter.service;

import com.example.ApiLetter.model.Follow;
import com.example.ApiLetter.model.User;
import com.example.ApiLetter.repository.FollowRepository;
import com.example.ApiLetter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowService {

    @Autowired
    private FollowRepository followRepo;

    @Autowired
    private UserRepository userRepo;

    // Seguir um usuário
    public Follow seguir(Long followerId, Long followedId) {
        if(followerId.equals(followedId)) throw new RuntimeException("Você não pode se seguir");

        User follower = userRepo.findById(followerId).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        User followed = userRepo.findById(followedId).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Verifica se já segue
        if(followRepo.existsByFollowerAndFollowed(follower, followed)) {
            throw new RuntimeException("Você já segue esse usuário");
        }

        Follow f = new Follow(follower, followed);
        return followRepo.save(f);
    }

    // Lista quem segue você
    public List<User> meusSeguidores(Long userId) {
        return followRepo.findByFollowedId(userId)
                .stream()
                .map(Follow::getFollower)
                .collect(Collectors.toList());
    }

    // Lista quem você segue
    public List<User> pessoasQueSigo(Long userId) {
        return followRepo.findByFollowerId(userId)
                .stream()
                .map(Follow::getFollowed)
                .collect(Collectors.toList());
    }
}

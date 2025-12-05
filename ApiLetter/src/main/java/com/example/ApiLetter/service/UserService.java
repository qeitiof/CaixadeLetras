package com.example.ApiLetter.service;

import com.example.ApiLetter.dto.UserCreateDTO;
import com.example.ApiLetter.dto.UserLoginResponseDTO;
import com.example.ApiLetter.dto.UserUpdateDTO;
import com.example.ApiLetter.model.User;
import com.example.ApiLetter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ApiLetter.dto.UserLoginDTO;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder encoder;

    public List<User> listAll() {
        return repo.findAll();
    }

    // GET ALL com paginação, ordenação e filtros
    public Page<User> listAll(Pageable pageable, String username, String email) {
        Specification<User> spec = Specification.where(null);
        
        if (username != null && !username.isEmpty()) {
            spec = spec.and((root, query, cb) -> 
                cb.like(cb.lower(root.get("username")), "%" + username.toLowerCase() + "%"));
        }
        if (email != null && !email.isEmpty()) {
            spec = spec.and((root, query, cb) -> 
                cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
        }
        
        return repo.findAll(spec, pageable);
    }

    public User findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public User create(UserCreateDTO dto) {

        validarSenhaForte(dto.getPassword());

        User u = new User();
        u.setUsername(dto.getUsername());
        u.setEmail(dto.getEmail());
        u.setPassword(encoder.encode(dto.getPassword()));

        return repo.save(u);
    }

    public User update(Long id, UserUpdateDTO dto) {

        return repo.findById(id).map(user -> {
            if (dto.getUsername() != null) {
                user.setUsername(dto.getUsername());
            }
            if (dto.getEmail() != null) {
                user.setEmail(dto.getEmail());
            }
            if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
                validarSenhaForte(dto.getPassword());
                user.setPassword(encoder.encode(dto.getPassword()));
            }

            return repo.save(user);
        }).orElse(null);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    private void validarSenhaForte(String senha) {
        if (senha.length() < 8 ||
                !senha.matches(".*[A-Z].*") ||
                !senha.matches(".*[a-z].*") ||
                !senha.matches(".*\\d.*") ||
                !senha.matches(".*[@#$%^&+=!].*")) {
            throw new IllegalArgumentException(
                    "A senha deve ter no mínimo 8 caracteres, incluir letra maiúscula, minúscula, número e símbolo."
            );
        }
    }

    @Autowired
    private FollowService followService;

    public UserLoginResponseDTO login(UserLoginDTO dto) {

        User user = repo.findByUsername(dto.getUsername());

        if (user == null) {
            throw new RuntimeException("Usuário não encontrado");
        }

        if (!encoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Senha incorreta");
        }

        // busca seguidores e seguindo
        List<User> seguidores = followService.meusSeguidores(user.getId());
        List<User> seguindo = followService.pessoasQueSigo(user.getId());

        return new UserLoginResponseDTO(user, seguidores, seguindo);
    }



}

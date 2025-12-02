package com.example.ApiLetter.dto;

import com.example.ApiLetter.model.User;
import java.util.List;

public class UserLoginResponseDTO {
    private Long id;
    private String username;
    private String email;
    private List<User> seguidores;
    private List<User> seguindo;

    public UserLoginResponseDTO(User user, List<User> seguidores, List<User> seguindo) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.seguidores = seguidores;
        this.seguindo = seguindo;
    }

    // getters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public List<User> getSeguidores() { return seguidores; }
    public List<User> getSeguindo() { return seguindo; }
}

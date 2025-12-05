package com.example.ApiLetter.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UserUpdateDTO {
    
    @Size(min = 3, max = 50, message = "O username deve ter entre 3 e 50 caracteres")
    private String username;
    
    @Email(message = "O email deve ser válido")
    private String email;
    
    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
    private String password;

    public UserUpdateDTO() {}

    public UserUpdateDTO(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

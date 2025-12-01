package com.example.ApiLetter.dto;

public record UserCreateDTO(
        String username,
        String email,
        String password
) {}

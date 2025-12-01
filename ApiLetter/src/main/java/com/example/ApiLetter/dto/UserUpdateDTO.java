package com.example.ApiLetter.dto;

public record UserUpdateDTO(
        String username,
        String email,
        String password
) {}

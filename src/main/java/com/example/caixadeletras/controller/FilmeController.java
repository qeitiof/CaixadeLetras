package com.example.caixadeletras.controller;

import com.example.caixadeletras.model.Filme;
import com.example.caixadeletras.service.FilmeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FilmeController {

    private final FilmeService filmeService;

    public FilmeController(FilmeService filmeService) {
        this.filmeService = filmeService;
    }

    @GetMapping("/filme")
    public Filme getFilme(@RequestParam String titulo) {
        return filmeService.buscarFilmePorTitulo(titulo);
    }
}

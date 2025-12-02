package com.example.ApiLetter.service;

import com.example.ApiLetter.dto.MovieDTO;
import com.example.ApiLetter.dto.OmdbSearchResponse;
import com.example.ApiLetter.model.Movie;
import com.example.ApiLetter.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FilmeService {

    @Value("${omdb.api.key}")
    private String omdbApiKey;

    private final RestTemplate restTemplate;
    private final MovieRepository movieRepository;

    public FilmeService(RestTemplate restTemplate, MovieRepository movieRepository) {
        this.restTemplate = restTemplate;
        this.movieRepository = movieRepository;
    }

    public MovieDTO buscarFilmePorTitulo(String titulo) {
        try {
            String url = "http://www.omdbapi.com/?apikey=" + omdbApiKey + "&t=" + titulo.replace(" ", "+");
            System.out.println("Buscando filme na API OMDB: " + url);

            MovieDTO dto = restTemplate.getForObject(url, MovieDTO.class);

            // Se não achar no OMDB, retorna null para o controller
            if (dto == null || dto.getImdbId() == null) {
                System.out.println("Filme não encontrado na API OMDB");
                return null;
            }

            // Verificar se já existe no banco
            Movie existente = movieRepository.findByImdbId(dto.getImdbId());

            // Se não existe → cria e salva
            if (existente == null) {
                Movie novo = new Movie(
                    dto.getTitle() != null ? dto.getTitle() : "", 
                    dto.getImdbId(), 
                    dto.getYear() != null ? dto.getYear() : "", 
                    dto.getPoster() != null ? dto.getPoster() : ""
                );
                movieRepository.save(novo);
            } else {
                // Atualiza informações se necessário
                if (existente.getYear() == null || existente.getPoster() == null) {
                    if (dto.getYear() != null) existente.setYear(dto.getYear());
                    if (dto.getPoster() != null) existente.setPoster(dto.getPoster());
                    movieRepository.save(existente);
                }
            }

            return dto;
        } catch (Exception e) {
            System.out.println("Erro ao buscar filme: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Busca sugestões de filmes com fallback para o banco de dados
     * 1. Tenta buscar da API OMDB
     * 2. Armazena no banco (sem duplicar)
     * 3. Se API falhar, busca do banco
     */
    public Map<String, Object> buscarSugestoes(String titulo) {
        Map<String, Object> resultado = new HashMap<>();
        List<Map<String, String>> sugestoes = new ArrayList<>();

        try {
            // Tentar buscar da API OMDB
            String url = "https://www.omdbapi.com/?apikey=" + omdbApiKey + "&s=" + titulo.replace(" ", "+");
            System.out.println("Buscando na API OMDB: " + url);
            
            OmdbSearchResponse response = restTemplate.getForObject(url, OmdbSearchResponse.class);
            
            if (response == null) {
                System.out.println("Resposta da API é null");
                return buscarDoBanco(titulo, resultado, sugestoes);
            }
            
            System.out.println("Response recebido - Response: " + response.getResponse() + ", Search size: " + (response.getSearch() != null ? response.getSearch().size() : "null"));

            if (response.getSearch() != null && "True".equals(response.getResponse())) {
                // API retornou sucesso - processar e armazenar
                for (OmdbSearchResponse.OmdbMovieItem item : response.getSearch()) {
                    if (item == null || item.getImdbId() == null) {
                        continue; // Pular itens inválidos
                    }
                    
                    // Verificar se já existe no banco (evitar duplicação)
                    Movie existente = movieRepository.findByImdbId(item.getImdbId());
                    
                    if (existente == null) {
                        // Não existe - criar novo
                        Movie novo = new Movie(
                            item.getTitle() != null ? item.getTitle() : "", 
                            item.getImdbId(), 
                            item.getYear() != null ? item.getYear() : "", 
                            item.getPoster() != null ? item.getPoster() : ""
                        );
                        movieRepository.save(novo);
                    } else {
                        // Existe - atualizar informações se necessário
                        if (existente.getYear() == null || existente.getPoster() == null) {
                            if (item.getYear() != null) existente.setYear(item.getYear());
                            if (item.getPoster() != null) existente.setPoster(item.getPoster());
                            movieRepository.save(existente);
                        }
                    }

                    // Adicionar à lista de sugestões
                    Map<String, String> sugestao = new HashMap<>();
                    sugestao.put("Title", item.getTitle() != null ? item.getTitle() : "");
                    sugestao.put("Year", item.getYear() != null ? item.getYear() : "");
                    sugestao.put("imdbID", item.getImdbId());
                    sugestao.put("Type", item.getType() != null ? item.getType() : "movie");
                    sugestao.put("Poster", item.getPoster() != null ? item.getPoster() : "");
                    sugestoes.add(sugestao);
                }

                resultado.put("Search", sugestoes);
                resultado.put("totalResults", response.getTotalResults() != null ? response.getTotalResults() : String.valueOf(sugestoes.size()));
                resultado.put("Response", "True");
                return resultado;
            } else {
                // API retornou mas sem resultados válidos
                System.out.println("API OMDB retornou sem resultados válidos");
            }

        } catch (RestClientException e) {
            // API falhou - buscar do banco como fallback
            System.out.println("API OMDB indisponível, buscando do banco de dados: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            // Qualquer outro erro
            System.out.println("Erro ao buscar sugestões: " + e.getMessage());
            e.printStackTrace();
        }

        // Fallback: buscar do banco de dados
        return buscarDoBanco(titulo, resultado, sugestoes);
    }
    
    private Map<String, Object> buscarDoBanco(String titulo, Map<String, Object> resultado, List<Map<String, String>> sugestoes) {
        try {
            List<Movie> filmesDoBanco = movieRepository.findByTituloContainingIgnoreCase(titulo);
            
            if (!filmesDoBanco.isEmpty()) {
                for (Movie filme : filmesDoBanco) {
                    Map<String, String> sugestao = new HashMap<>();
                    sugestao.put("Title", filme.getTitulo() != null ? filme.getTitulo() : "");
                    sugestao.put("Year", filme.getYear() != null ? filme.getYear() : "");
                    sugestao.put("imdbID", filme.getImdbId() != null ? filme.getImdbId() : "");
                    sugestao.put("Type", "movie");
                    sugestao.put("Poster", filme.getPoster() != null ? filme.getPoster() : "");
                    sugestoes.add(sugestao);
                }

                resultado.put("Search", sugestoes);
                resultado.put("totalResults", String.valueOf(filmesDoBanco.size()));
                resultado.put("Response", "True");
                return resultado;
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar do banco: " + e.getMessage());
            e.printStackTrace();
        }

        // Nenhum resultado encontrado
        resultado.put("Search", new ArrayList<>());
        resultado.put("totalResults", "0");
        resultado.put("Response", "False");
        resultado.put("Error", "Nenhum filme encontrado");
        return resultado;
    }
}

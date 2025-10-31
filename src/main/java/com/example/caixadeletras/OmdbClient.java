package com.example.caixadeletras;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class OmdbClient {

    // 🔹 Usa a nova API key e o parâmetro certo (t= para buscar por título)
    private static final String API_URL = "http://www.omdbapi.com/?apikey=e110f0dc&t=";

    public static void main(String[] args) throws IOException, InterruptedException {
        String titulo = "Se7en"; // Título do filme (pode testar "Se7en", "Inception", etc.)
        String url = API_URL + titulo.replace(" ", "+");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }
}

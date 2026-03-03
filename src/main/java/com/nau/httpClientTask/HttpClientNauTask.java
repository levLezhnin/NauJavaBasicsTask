package com.nau.httpClientTask;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nau.NauTask;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class HttpClientNauTask implements NauTask {

    @Override
    public void solve() {

        URI requestUri = URI.create("https://httpbin.org/user-agent");
        CompletableFuture<HttpResponse<String>> getUserAgentAsyncRequest;

        try (HttpClient client = HttpClient.newHttpClient()) {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(requestUri)
                    .build();

            getUserAgentAsyncRequest = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

            getUserAgentAsyncRequest
                    .orTimeout(10, TimeUnit.SECONDS)
                    .thenApply(HttpResponse::body)
                    .thenAccept(body -> {
                        ObjectMapper objectMapper = new ObjectMapper();
                        try {
                            UserAgentData userAgentData = objectMapper.readValue(body, UserAgentData.class);
                            System.out.println("Идентификационная строка приложения, с которого выполялся запрос: " + userAgentData.getUserAgent());
                        } catch (JsonProcessingException e) {
                            System.err.printf("Не получилось прочитать данные о идентификационной строке из тела ответа %s. Причина: %s", body, e.getMessage());
                        }
                    })
                    .exceptionally(e -> {
                        if (e instanceof TimeoutException) {
                            System.err.println("Таймаут запроса");
                        } else {
                            System.err.println("Ошибка: " + e.getMessage());
                        }
                        return null;
                    })
                    .join();
        }
    }

}


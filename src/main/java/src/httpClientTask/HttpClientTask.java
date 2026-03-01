package src.httpClientTask;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import src.NauTask;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class HttpClientTask implements NauTask {

    @Override
    public void solve() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

        CompletableFuture<HttpResponse<String>> getUserAgent = asyncHttpClient.sendGetRequestAsync(URI.create("https://httpbin.org/user-agent"));

        getUserAgent
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
                });
    }

}

class UserAgentData {

    @JsonProperty("user-agent")
    private String userAgent;

    public String getUserAgent() {
        return userAgent;
    }

    public UserAgentData() {}

    public UserAgentData(String userAgent) {
        this.userAgent = userAgent;
    }
}

class AsyncHttpClient {

    public CompletableFuture<HttpResponse<String>> sendGetRequestAsync(URI uri) {
        try (HttpClient client = HttpClient.newHttpClient())
        {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .build();
            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        }
    }
}
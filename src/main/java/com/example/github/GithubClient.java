package com.example.github;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class GithubClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String baseUrl;

    public GithubClient(
            @Value("${github-api.base-url}") String baseUrl,
            ObjectMapper objectMapper
    ) {
        this.baseUrl = baseUrl;
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newHttpClient();
    }

    public GithubRepository[] getRepositories(String username) {
        HttpRequest request = baseRequest(
                "/users/%s/repos".formatted(username)
        );

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 404) {
                throw new UserNotFoundException(username);
            }

            ensureSuccess(response);
            return objectMapper.readValue(response.body(), GithubRepository[].class);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to fetch repositories", e);
        }
    }

    public GithubBranch[] getRepositoryBranches(String owner, String repo) {
        HttpRequest request = baseRequest(
                "/repos/%s/%s/branches".formatted(owner, repo)
        );

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            ensureSuccess(response);
            return objectMapper.readValue(response.body(), GithubBranch[].class);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to fetch branches", e);
        }
    }

    private HttpRequest baseRequest(String path) {
        return HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .header("User-Agent", "github-api-proxy")
                .header("Accept", "application/vnd.github+json")
                .header("X-GitHub-Api-Version", "2022-11-28")
                .GET()
                .build();
    }

    private static void ensureSuccess(HttpResponse<?> response) {
        if (response.statusCode() / 100 != 2) {
            throw new RuntimeException(
                    "GitHub API error: HTTP " + response.statusCode()
            );
        }
    }
}

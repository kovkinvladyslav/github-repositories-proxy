package com.example.github;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class GithubClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final GithubApiProperties properties;

    public GithubRepository[] getRepositories(String username) {
        return get("/users/%s/repos".formatted(username),
                GithubRepository[].class, username);
    }

    public GithubBranch[] getRepositoryBranches(String owner, String repo) {
        return get("/repos/%s/%s/branches".formatted(owner, repo),
                GithubBranch[].class, owner);
    }

    private <T> T get(String path, Class<T> responseType, String userFor404) {
        HttpRequest request = baseRequest(path);

        try {
            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 404) {
                log.warn("GitHub returned 404 for user={}", userFor404);
                throw new UserNotFoundException(userFor404);
            }

            if (response.statusCode() / 100 != 2) {
                log.error("GitHub API error {} for path={}",
                        response.statusCode(), path);
                throw new RuntimeException("GitHub API error: HTTP " + response.statusCode());
            }

            return objectMapper.readValue(response.body(), responseType);

        } catch (IOException | InterruptedException e) {
            log.error("Failed to call GitHub API for path={}", path, e);
            throw new RuntimeException("Failed to call GitHub API", e);
        }
    }

    private HttpRequest baseRequest(String path) {
        return HttpRequest.newBuilder()
                .uri(URI.create(properties.baseUrl() + path))
                .header("User-Agent", properties.userAgent())
                .header("Accept", "application/vnd.github+json")
                .header("X-GitHub-Api-Version", properties.apiVersion())
                .GET()
                .build();
    }
}

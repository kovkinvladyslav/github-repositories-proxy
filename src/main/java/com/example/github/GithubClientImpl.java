package com.example.github;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
public final class GithubClientImpl implements GithubClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final GithubApiProperties properties;

    @Override
    public GithubRepository[] getRepositories(final String username) {
        return get("/users/%s/repos".formatted(username),
                GithubRepository[].class, username);
    }

    @Override
    public GithubBranch[] getRepositoryBranches(
            final String owner, final String repo
    ) {
        return get("/repos/%s/%s/branches".formatted(owner, repo),
                GithubBranch[].class, owner);
    }

    private <T> T get(
            final String path,
            final Class<T> responseType,
            final String userFor404
    ) {
        HttpRequest request = baseRequest(path);

        try {
            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() == HttpStatus.NOT_FOUND.value()) {
                log.warn("GitHub returned 404 for user={}", userFor404);
                throw new UserNotFoundException(userFor404);
            }

            if (response.statusCode() != HttpStatus.OK.value()) {
                log.error(
                        "GitHub API error {} for path={}",
                        response.statusCode(),
                        path
                );

                final String message =
                        "GitHub API error: HTTP " + response.statusCode();

                throw new InternalException(message);
            }


            return objectMapper.readValue(response.body(), responseType);

        } catch (IOException e) {
            log.error("Failed to call GitHub API for path={}", path, e);
            throw new InternalException("Failed to call GitHub API", e);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn(
                    "Thread interrupted while calling GitHub API for path={}",
                    path
            );
            throw new InternalException(
                    "Thread interrupted while calling GitHub API",
                    e
            );
        }

    }

    private HttpRequest baseRequest(final String path) {
        return HttpRequest.newBuilder()
                .uri(URI.create(properties.baseUrl() + path))
                .header("User-Agent", properties.userAgent())
                .header("Accept", "application/vnd.github+json")
                .header("X-GitHub-Api-Version", properties.apiVersion())
                .GET()
                .build();
    }
}

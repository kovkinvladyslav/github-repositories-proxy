package com.example.github;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
public class GithubClient {

    private final RestClient restClient;

    public GithubClient(
            RestClient.Builder builder,
            @Value("${github-api.base-url}") String baseUrl
    ) {
        this.restClient = builder
                .baseUrl(baseUrl)
                .defaultHeader("User-Agent", "github-api-proxy")
                .defaultHeader("Accept", "application/vnd.github+json")
                .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
                .build();
    }

    public GithubRepository[] getRepositories(String username) {
        try {
            return restClient.get()
                    .uri("/users/{username}/repos", username)
                    .retrieve()
                    .body(GithubRepository[].class);

        } catch (HttpClientErrorException.NotFound e) {
            throw new UserNotFoundException(username);
        }
    }

    public GithubBranch[] getRepositoryBranches(String owner, String repo) {
        return restClient.get()
                .uri("/repos/{owner}/{repo}/branches", owner, repo)
                .retrieve()
                .body(GithubBranch[].class);
    }
}

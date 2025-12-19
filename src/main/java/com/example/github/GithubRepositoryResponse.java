package com.example.github;

public record GithubRepositoryResponse(String name, Owner owner, boolean fork) {
    record Owner(String login) { }
}

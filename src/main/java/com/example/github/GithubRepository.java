package com.example.github;

public record GithubRepository(String name, Owner owner, boolean fork) {
    record Owner(String login) { }
}

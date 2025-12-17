package com.example.github;

public record GithubBranch(String name, Commit commit) {
    record Commit(String sha) {}
}

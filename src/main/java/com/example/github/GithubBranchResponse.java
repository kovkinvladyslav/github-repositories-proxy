package com.example.github;

public record GithubBranchResponse(String name, Commit commit) {
    record Commit(String sha) { }
}

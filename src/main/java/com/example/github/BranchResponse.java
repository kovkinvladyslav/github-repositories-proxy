package com.example.github;

public record BranchResponse(
        String name,
        String lastCommitSha
) {
}

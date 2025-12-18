package com.example.github;

/**
 * Response containing branch information.
 *
 * @param name branch name
 * @param lastCommitSha SHA of the last commit on this branch
 */
public record BranchResponse(
        String name,
        String lastCommitSha
) { }

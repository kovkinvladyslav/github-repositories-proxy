package com.example.github;

/**
 * Client for interacting with GitHub REST API.
 */
public interface GithubClient {
    /**
     * Retrieves all repositories for a GitHub user.
     *
     * @param username GitHub username
     * @return array of repositories
     */
    GithubRepositoryResponse[] getRepositories(String username);

    /**
     * Retrieves all branches for a repository.
     *
     * @param username GitHub username (repository owner)
     * @param repo repository name
     * @return array of branches
     */
    GithubBranchResponse[] getRepositoryBranches(String username, String repo);
}

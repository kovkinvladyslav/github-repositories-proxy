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
    GithubRepository[] getRepositories(String username);

    /**
     * Retrieves all branches for a repository.
     *
     * @param owner repository owner
     * @param repo repository name
     * @return array of branches
     */
    GithubBranch[] getRepositoryBranches(String owner, String repo);
}

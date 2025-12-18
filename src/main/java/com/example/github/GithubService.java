package com.example.github;

import java.util.List;

/**
 * Service for retrieving GitHub repositories.
 */
public interface GithubService {
    /**
     * Retrieves all non-fork repositories for a GitHub user.
     *
     * @param username GitHub username
     * @return list of repositories with branch information
     */
    List<RepositoryResponse> getRepositories(String username);
}

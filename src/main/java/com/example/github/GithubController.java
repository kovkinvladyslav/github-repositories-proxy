package com.example.github;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for GitHub repositories proxy API.
 */
@RestController
@RequestMapping("/github")
@RequiredArgsConstructor
public final class GithubController {

    private final GithubService service;

    /**
     * Retrieves all non-fork repositories for a GitHub user.
     *
     * @param username GitHub username
     * @return list of repositories with branch information
     */
    @GetMapping("/{username}/repositories")
    public List<RepositoryResponse>
    getRepositories(@PathVariable final String username) {
        return service.getRepositories(username);
    }
}

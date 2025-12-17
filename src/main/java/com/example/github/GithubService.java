package com.example.github;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class GithubService {

    private final GithubClient client;

    public GithubService(GithubClient client) {
        this.client = client;
    }

    public List<RepositoryResponse> getRepositories(String username) {
        return Arrays.stream(client.getRepositories(username))
                .filter(repo -> !repo.fork())
                .map(this::mapToResponse)
                .toList();
    }

    private RepositoryResponse mapToResponse(GithubRepository repo) {
        var branches = client.getRepositoryBranches(
                repo.owner().login(),
                repo.name());

        List<BranchResponse> branchResponses = Arrays.stream(branches)
                .map(b -> new BranchResponse(b.name(), b.commit().sha()))
                .toList();

        return new RepositoryResponse(
                repo.name(),
                repo.owner().login(),
                branchResponses
        );
    }
}
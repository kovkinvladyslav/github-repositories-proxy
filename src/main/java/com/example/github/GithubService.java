package com.example.github;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GithubService {

    private final GithubClient client;

    public GithubService(GithubClient client) {
        this.client = client;
    }

    public List<RepositoryResponse> getRepositories(String username) {
        var repos = client.getRepositories(username);

        List<RepositoryResponse> repositoryResponse = new ArrayList<>();
        for (var repo : repos) {
            if (repo.fork()) continue;

            var branches = client.getRepositoryBranches(
                    repo.owner().login(),
                    repo.name()
            );

            repositoryResponse.add(new RepositoryResponse(
                    repo.name(),
                    repo.owner().login(),
                    Arrays.stream(branches).map(branch -> new BranchResponse(
                            branch.name(),
                            branch.commit().sha())).toList()));
        }
        return repositoryResponse;
    }
}

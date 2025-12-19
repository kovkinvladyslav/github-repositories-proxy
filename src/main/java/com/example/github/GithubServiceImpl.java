package com.example.github;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public final class GithubServiceImpl implements GithubService {

    private final GithubClient client;

    @Override
    public List<RepositoryResponse> getRepositories(final String username) {
        log.debug("Fetching repositories for user={}", username);

        var repos = client.getRepositories(username);

        var result = Arrays.stream(repos)
                .filter(repo -> !repo.fork())
                .map(repo -> new RepositoryResponse(
                        repo.name(),
                        repo.owner().login(),
                        getBranches(repo)))
                .toList();

        log.debug(
                "Found {} non-fork repositories for user={}",
                result.size(), username
        );

        return result;
    }

    private List<BranchResponse>
    getBranches(final GithubRepositoryResponse repo) {
        log.debug("Fetching branches for repo={} owner={}",
                repo.name(), repo.owner().login());

        var branches = client.getRepositoryBranches(
                repo.owner().login(),
                repo.name()
        );

        return Arrays.stream(branches)
                .map(branch -> new BranchResponse(
                        branch.name(),
                        branch.commit().sha()
                ))
                .toList();
    }
}

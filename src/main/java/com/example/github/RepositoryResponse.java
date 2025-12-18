package com.example.github;

import java.util.List;

/**
 * Response containing repository information with branches.
 *
 * @param repositoryName name of the repository
 * @param ownerLogin login of the repository owner
 * @param branches list of branches with their last commit SHA
 */
public record RepositoryResponse(
        String repositoryName,
        String ownerLogin,
        List<BranchResponse> branches
) {
}

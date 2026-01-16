package com.example.demo.model;

import java.util.List;

public record UsersGithubRepoDTO(
        String repositoryName,
        String ownerLogin,
        List<BranchDTO> Branches
) {

    public record BranchDTO (
            String name,
            String sha
    ) { }
}



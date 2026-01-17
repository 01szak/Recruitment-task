package com.example.demo.model;

import java.util.List;

public record UsersGithubRepoDTO(
        String repositoryName,
        String ownerLogin,
        List<BranchDTO> branches
) {

    public record BranchDTO (
            String name,
            String sha
    ) { }
}



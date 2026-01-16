package com.example.demo.model;

import java.util.List;

public record Response(
        String ownerLogin,
        List<Repositories> repositories
) {
    public record Repositories(
            String name,
            List<Branches> branches
    ) { }

    public record Branches(
            String name,
            String sha
    ) { }
}



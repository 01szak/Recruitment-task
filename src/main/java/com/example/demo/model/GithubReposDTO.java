package com.example.demo.model;

public record GithubReposDTO(
    Owner owner,
    String name
) {
    public record Owner(String login){}
    public record Branch(String name, Commit commit){}
    public record Commit(String sha){ }
}

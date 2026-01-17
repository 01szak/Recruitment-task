package com.example.demo.service;

public enum GithubConstant {

    ACCEPT_HEADER("application/vnd.github+json"),
    GET_REPOS_PATH("/api.github.com/users/%s/repos"),
    GET_BRANCHES_PATH("/api.github.com/repos/%s/%s/branches");

    private final String value;

    GithubConstant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    public String getValue(Object... args) {
        return args.length == 0 ? getValue() : String.format(value, args);
    }
}

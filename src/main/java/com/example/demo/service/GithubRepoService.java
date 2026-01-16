package com.example.demo.service;

import com.example.demo.model.Response;

public interface GithubRepoService {

    Response getUsersGithubRepositories(String username);
}

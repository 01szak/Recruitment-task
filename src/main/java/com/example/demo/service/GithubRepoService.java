package com.example.demo.service;

import com.example.demo.model.UsersGithubRepoDTO;

import java.util.List;

public interface GithubRepoService {

    List<UsersGithubRepoDTO> getUsersGithubRepositories(String username);
}

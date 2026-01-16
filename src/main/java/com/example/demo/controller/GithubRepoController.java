package com.example.demo.controller;

import com.example.demo.model.UsersGithubRepoDTO;
import com.example.demo.service.GithubRepoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/repo")
public class GithubRepoController {

    private final GithubRepoService githubRepoService;

    public GithubRepoController(GithubRepoService githubRepoService) {
        this.githubRepoService = githubRepoService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<UsersGithubRepoDTO>> getAllUserRepoWithNoForks(@PathVariable String username) {
        var usersRepositories = githubRepoService.getUsersGithubRepositories(username);
        return ResponseEntity.ok(usersRepositories);
    }
}

package com.example.demo.controller;

import com.example.demo.model.Response;
import com.example.demo.model.Request;
import com.example.demo.service.GithubRepoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GithubRepoController {

    private final GithubRepoService githubRepoService;

    public GithubRepoController(GithubRepoService githubRepoService) {
        this.githubRepoService = githubRepoService;
    }

    @GetMapping
    public ResponseEntity<Response> getAllUserRepoWithNoForks(@RequestBody Request request) {
        var usersRepositories = githubRepoService.getUsersGithubRepositories(request.username());
        return ResponseEntity.ok(usersRepositories);
    }
}

package com.example.demo.service;

import com.example.demo.model.GithubReposDTO;
import com.example.demo.model.UsersGithubRepoDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GithubRepoServiceImpl implements GithubRepoService {

    private final RestTemplate restTemplate;

    private static final String ACCEPT_HEADER = "application/vnd.github+json";
    private static final String GET_REPOS_PATH = "https://api.github.com/users/%s/repos";
    private static final String GET_BRANCHES_PATH = "https://api.github.com/repos/%s/%s/branches";

    public GithubRepoServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<UsersGithubRepoDTO> getUsersGithubRepositories(String username) throws HttpClientErrorException {
        var userRepos = new ArrayList<UsersGithubRepoDTO>();
        var githubReposPtr = new ParameterizedTypeReference<List<GithubReposDTO>>(){};
        var branchesPtr = new ParameterizedTypeReference<List<GithubReposDTO.Branch>>(){};

        var httpEntity = new HttpEntity<>(getHeaders());

        var reposResponse =  restTemplate.exchange(
                String.format(GET_REPOS_PATH, username),
                HttpMethod.GET,
                httpEntity,
                githubReposPtr
        ).getBody();

        if (reposResponse == null) {
            throw new IllegalStateException("Response from Github is null, try again!");
        }


        for (var repo : reposResponse) {
            var branchesResponse =  restTemplate.exchange(
                    String.format(GET_BRANCHES_PATH, username, repo.name()),
                    HttpMethod.GET,
                    httpEntity,
                    branchesPtr
            ).getBody();

            if (branchesResponse == null) {
                throw new IllegalStateException("Response from Github is null, try again!");
            }

            var branchesDTO = branchesResponse.stream()
                    .map(b -> new UsersGithubRepoDTO.BranchDTO(b.name(), b.commit().sha()))
                    .toList();

            userRepos.add(new UsersGithubRepoDTO(repo.name(), repo.owner().login(), branchesDTO));
        }

        return userRepos;
    }

    private HttpHeaders getHeaders() {
        var headers = new HttpHeaders();
        headers.set("Accept", ACCEPT_HEADER);
        return headers;
    }
}

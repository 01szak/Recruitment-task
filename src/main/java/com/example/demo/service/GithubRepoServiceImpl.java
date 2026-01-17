package com.example.demo.service;

import com.example.demo.model.GithubReposDTO;
import com.example.demo.model.UsersGithubRepoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.example.demo.service.GithubConstant.*;

@Service
public class GithubRepoServiceImpl implements GithubRepoService {

    @Value("${github.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    public GithubRepoServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<UsersGithubRepoDTO> getUsersGithubRepositories(String username) throws HttpClientErrorException {
        var userRepos = new ArrayList<UsersGithubRepoDTO>();
        var githubReposPtr = new ParameterizedTypeReference<List<GithubReposDTO>>(){};
        var branchesPtr = new ParameterizedTypeReference<List<GithubReposDTO.Branch>>(){};
        var httpEntity = new HttpEntity<>(getHeaders());
        var path = baseUrl + GET_REPOS_PATH.getValue(username);

        var reposResponse =  restTemplate.exchange(
                path,
                HttpMethod.GET,
                httpEntity,
                githubReposPtr
        ).getBody();

        if (reposResponse == null) {
            throw new IllegalStateException("Github returned any repos, try again!");
        }


        for (var repo : reposResponse) {
            path = baseUrl + GET_BRANCHES_PATH.getValue(username, repo.name());
            var branchesResponse =  restTemplate.exchange(
                    path,
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
        headers.set("Accept", ACCEPT_HEADER.getValue());
        return headers;
    }
}

package com.example.demo.service;

import com.example.demo.model.Request;
import com.example.demo.model.Response;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class GithubRepoServiceImpl implements GithubRepoService {

    private final RestTemplate restTemplate;

    private static final String ACCEPT_HEADER = "application/vnd.github+json";
    private static final String PATH = "https://api.github.com/users/%s/repos";

    public GithubRepoServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Response getUsersGithubRepositories(String username) throws HttpClientErrorException {
        var httpEntity = new HttpEntity<>(getHeaders());
        var repos = new ParameterizedTypeReference<Response>(){};

        return restTemplate.exchange(
                String.format(PATH, username),
                HttpMethod.GET,
                httpEntity,
                repos
        ).getBody();
    }

    private HttpHeaders getHeaders() {
        var headers = new HttpHeaders();
        headers.set("Accept", ACCEPT_HEADER);
        return headers;
    }
}

package com.example.demo;

import com.example.demo.model.UsersGithubRepoDTO;
import com.example.demo.service.GithubRepoService;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static com.example.demo.service.GithubConstant.GET_BRANCHES_PATH;
import static com.example.demo.service.GithubConstant.GET_REPOS_PATH;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
@ActiveProfiles("test")
@WireMockTest(httpPort = 8081)
class RecruitmentTaskApplicationTests {

	@Autowired
	private GithubRepoService githubRepoService;

	private static final String OWNER_LOGIN = "octocat";
	private static final String REPO_NAME = "Hello-World";
	private static final String BRANCH_NAME = "master";
	private static final String SHA = "c5b97d5ae6c19d5c5df71a34c7fbeeda2479ccbc";
	private static final String WRONG_USERNAME = "wrongUsername";
	private static final String EXPECTED_ERROR_MESSAGE =
			"404 Not Found on GET request for \"http://localhost:8081/api.github.com/users/wrongUsername/repos\": \"404 Not Found on GET request for \"https://localhost:8081/api.github.com/users/wrongUsername/repos\": {\"message\":\"Not Found\",\"documentation_url\":\"https://docs.github.com/rest\",\"status\":\"404\"}\"";

	//shortened api response from https://docs.github.com/en/rest/repos/repos?apiVersion=2022-11-28
	private static final String GITHUB_REPO_RESPONSE_OBJECT = """
			[
			  {
				"id": 1296269,
				"node_id": "MDEwOlJlcG9zaXRvcnkxMjk2MjY5",
				"name": "Hello-World",
				"owner": {
				  "login": "octocat"
				}
			  }
			]
			""";
	private static final String GITHUB_BRANCH_RESPONSE_OBJECT = """
			[
			  {
				"name": "master",
				"commit": {
				  "sha": "c5b97d5ae6c19d5c5df71a34c7fbeeda2479ccbc"
				}
			  }
			]
			""";

	@Test
	void shouldReturnMappedGithubReposAndBranches() {
		stubFor(get(urlEqualTo(GET_REPOS_PATH.getValue(OWNER_LOGIN)))
				.willReturn(aResponse()
						.withHeader("Content-Type", "application/json")
						.withStatus(200)
						.withBody(GITHUB_REPO_RESPONSE_OBJECT)));

		stubFor(get(urlEqualTo(GET_BRANCHES_PATH.getValue(OWNER_LOGIN, REPO_NAME)))
				.willReturn(aResponse()
						.withHeader("Content-Type", "application/json")
						.withStatus(200)
						.withBody(GITHUB_BRANCH_RESPONSE_OBJECT)));


		List<UsersGithubRepoDTO> actual = githubRepoService.getUsersGithubRepositories(OWNER_LOGIN);

		assertNotNull(actual);
		assertEquals(1, actual.size());

		var actualRepo = actual.getFirst();

		assertEquals(REPO_NAME, actualRepo.repositoryName());
		assertEquals(OWNER_LOGIN, actualRepo.ownerLogin());

		assertEquals(1, actualRepo.Branches().size());

		var actualBranch = actualRepo.Branches().getFirst();

		assertEquals(BRANCH_NAME, actualBranch.name());
		assertEquals(SHA, actualBranch.sha());
	}

	@Test
	void shouldThrowHttpClientErrorExceptionWithCorrectStatusCodeAndMessage() {
		stubFor(get(urlEqualTo(GET_REPOS_PATH.getValue(WRONG_USERNAME)))
				.willReturn(aResponse()
						.withStatus(404)
						.withBody(EXPECTED_ERROR_MESSAGE)));

		HttpClientErrorException actualException =
				assertThrows(HttpClientErrorException.class,
					() -> githubRepoService.getUsersGithubRepositories(WRONG_USERNAME));

		assertEquals(HttpStatusCode.valueOf(404), actualException.getStatusCode());
		assertTrue(
				actualException.getLocalizedMessage()
						.contains("404 Not Found on GET request for \"http://localhost:8081/api.github.com/users/wrongUsername/repos\":")
		);
	}



}

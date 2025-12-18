package com.example.github;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestClient;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
final class GithubApiProxyIntegrationTest {

    @LocalServerPort
    private int port;

    private RestClient restClient;

    @RegisterExtension
    static WireMockExtension wireMock =
            WireMockExtension.newInstance()
                    .options(wireMockConfig().dynamicPort())
                    .build();

    @DynamicPropertySource
    static void wiremockProps(final DynamicPropertyRegistry registry) {
        registry.add("github-api.base-url", wireMock::baseUrl);
    }

    @BeforeEach
    void setUpClient() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void getRepositories_UserNotFound() {
        // given
        String userName = "nonExistentOwnerLogin";

        // when
        var response = restClient
                .get()
                .uri("github/{userName}/repositories", userName)
                .exchange((request,httpResponse) -> ResponseEntity
                        .status(httpResponse.getStatusCode())
                        .body(httpResponse.bodyTo(ErrorResponse.class)));

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorResponse error = response.getBody();
        assertNotNull(error);
        assertEquals(404, error.status());
        assertEquals("User not found: " + userName, error.message());
    }

    @Test
    void getRepositories_onlyForks_returnsEmptyList() {
        // given
        String userName = "userWithOnlyForks";

        // when
        ResponseEntity<RepositoryResponse[]> response =
                restClient.get()
                        .uri("/github/{userName}/repositories", userName)
                        .retrieve()
                        .toEntity(RepositoryResponse[].class);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());

        RepositoryResponse[] repos = response.getBody();
        assertNotNull(repos);
        assertEquals(0, repos.length);
    }

    @Test
    void getRepositories_githubInternalError_returns500() {
        // given
        String userName = "gitHubError";

        // when
        var response = restClient
                .get()
                .uri("/github/{userName}/repositories", userName)
                .exchange((request, httpResponse) -> ResponseEntity
                        .status(httpResponse.getStatusCode())
                        .body(httpResponse.bodyTo(String.class)));

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void getRepositories_mixedRepos_filtersForks() {
        // given
        String userName = "mixedUser";

        // when
        ResponseEntity<RepositoryResponse[]> response =
                restClient.get()
                        .uri("/github/{userName}/repositories", userName)
                        .retrieve()
                        .toEntity(RepositoryResponse[].class);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());

        RepositoryResponse[] repos = response.getBody();
        assertNotNull(repos);
        assertEquals(1, repos.length);

        RepositoryResponse repo = repos[0];
        assertEquals("real-repo", repo.repositoryName());
        assertEquals("mixedUser", repo.ownerLogin());
        assertNotNull(repo.branches());
        assertEquals(0, repo.branches().size());
    }
}

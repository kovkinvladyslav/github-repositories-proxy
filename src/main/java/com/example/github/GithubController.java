package com.example.github;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/github")
@RequiredArgsConstructor
public class GithubController {

    private final GithubService service;

    @GetMapping("/{username}/repositories")
    public List<RepositoryResponse> getRepositories(
            @PathVariable String username
    ){
        return service.getRepositories(username);
    }
}

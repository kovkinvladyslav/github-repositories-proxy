package com.example.github;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "github-api")
public record GithubApiProperties(
        String baseUrl,
        String userAgent,
        String apiVersion
) { }

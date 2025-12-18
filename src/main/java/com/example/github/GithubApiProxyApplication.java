package com.example.github;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class GithubApiProxyApplication {
	public static void main(final String[] args) {
		SpringApplication.run(GithubApiProxyApplication.class, args);
	}
}

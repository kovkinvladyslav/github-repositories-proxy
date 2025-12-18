package com.example.github;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;

import java.net.http.HttpClient;

@SpringBootApplication
@ConfigurationPropertiesScan
public class GithubApiProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(GithubApiProxyApplication.class, args);
	}

	@Bean
	HttpClient httpClient() {
		return HttpClient.newHttpClient();
	}
}

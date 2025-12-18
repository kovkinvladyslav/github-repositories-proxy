# GitHub Repositories Proxy

Spring Boot proxy for GitHub REST API that returns non-fork repositories with branch name and last commit SHA for each branch.

## Requirements

- Java 25
- Maven 3.6+ (or use `./mvnw`)

## Build & Run

```bash
./mvnw clean install
./mvnw spring-boot:run
```

Application runs on `http://localhost:8080`

## API

**GET** `/github/{username}/repositories`

Returns all non-fork repositories for a GitHub user. For each repository includes repository name, owner login, and for each branch: branch name and last commit SHA.

**Example:**
```bash
curl http://localhost:8080/github/octocat/repositories
```

**Response:**
```json
[
  {
    "repositoryName": "Hello-World",
    "ownerLogin": "octocat",
    "branches": [
      {
        "name": "main",
        "lastCommitSha": "6dcb09b5b57875f334f61aebed695e2e4193db5e"
      }
    ]
  }
]
```

**Error Response (404):**
  ```json
  {
    "status": 404,
    "message": "User not found: username"
}
```

## Testing

```bash
./mvnw test
```

Integration tests use WireMock to mock GitHub API responses.

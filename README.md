# Shelfie

A personal book tracker built to practise the topics from 3rd semester at EK:
a Spring Boot REST API, JPA, a plain JavaScript frontend, Docker, GitHub Actions and cloud deployment.

## Status

**v0.1.0 – Walking skeleton**

- `GET /api/books` returns seeded books from an in-memory H2 database
- A plain HTML/JavaScript page fetches the books and renders them with Bootstrap

See the project plan for the coming phases (Docker, JPA relations, Open Library integration, SPA, CI/CD, cloud, security and HTTPS).

## Tech stack

| Part | Technology |
| --- | --- |
| Backend | Java 25, Spring Boot 4.1, Spring Web, Spring Data JPA, Maven |
| Database | H2 in-memory (phase 1), MySQL in Docker from phase 2 |
| Frontend | HTML, plain JavaScript (ES modules), Bootstrap 5 |
| Testing | JUnit, AssertJ, `@SpringBootTest` |

## Run it locally

1. **Start the backend:** open the project in IntelliJ and run `ShelfieApplication`.
   Check it at http://localhost:8080/api/books – you should see a JSON list of books.
2. **Open the frontend:** open `frontend/index.html` in IntelliJ and click a browser icon in the top-right corner.
   It opens on `http://localhost:63342/...`, which is allowed by the CORS configuration.

Run the tests with `./mvnw test` (or right-click `src/test/java` → Run All Tests).

> The H2 database is in-memory: data is reset on every restart and re-seeded at startup.

## Project structure

```
shelfie/
├── src/main/java/org/example/shelfie/
│   ├── ShelfieApplication.java
│   ├── book/      Book entity, repository, service, DTO, controller, seeder
│   └── common/    shared configuration (CORS)
├── src/test/java/  tests
├── frontend/       index.html, js/, css/
└── pom.xml
```

The backend is organised **package by feature**: everything about books lives in `book`,
and only the service, controller and DTO are public to the rest of the app.

## API

| Method | Path | Response |
| --- | --- | --- |
| GET | `/api/books` | `200 OK` – JSON array of books (`id`, `title`, `author`, `publishYear`) |

## Workflow

- `main` is protected: all changes go through a feature branch and a pull request
- Commit messages follow [Conventional Commits](https://www.conventionalcommits.org/) (`feat:`, `fix:`, `docs:`, `test:`, `chore:`)
- Each finished phase is tagged with a semantic version (`v0.1.0`, `v0.2.0`, …) and published as a GitHub Release
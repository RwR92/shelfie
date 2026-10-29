# Shelfie

A personal book tracker built to practise the topics from 3rd semester at EK:
a Spring Boot REST API, JPA, a plain JavaScript frontend, Docker, GitHub Actions and cloud deployment.

## Status

**v0.2.0 – Docker locally**

- `GET /api/books` returns seeded books from a MySQL database
- A plain HTML/JavaScript page fetches the books and renders them with Bootstrap
- The whole stack runs with Docker Compose: Nginx (frontend + reverse proxy), Spring Boot and MySQL

## Tech stack

| Part | Technology |
| --- | --- |
| Backend | Java 25, Spring Boot 4.1, Spring Web, Spring Data JPA, Maven |
| Database | MySQL 8.4 in Docker (H2 in-memory for tests) |
| Frontend | HTML, plain JavaScript (ES modules), Bootstrap 5, served by Nginx |
| Infrastructure | Docker (multi-stage builds), Docker Compose |
| Testing | JUnit, AssertJ, `@SpringBootTest` |

## Run with Docker (full stack)

1. Copy `.env.example` to `.env` and fill in the passwords.
2. Start everything:
   ```
   docker compose up -d --build
   ```
3. Open http://localhost

Stop it with `docker compose down`. The database data is kept in the `db-data` volume
(`docker compose down -v` deletes it).

## Run in dev mode

Faster for day-to-day coding: only MySQL runs in Docker, the backend runs from IntelliJ.

1. Start the database: `docker compose up -d db`
2. Run `ShelfieApplication` in IntelliJ. It connects to MySQL on `localhost:3306`.
3. Open `frontend/index.html` in IntelliJ and click a browser icon (opens on `http://localhost:63342`,
   allowed by the CORS configuration).

Run the tests with `.\mvnw test`. Tests use an in-memory H2 database, so MySQL doesn't need to be running.

## Architecture

```
Browser ──> Nginx (:80) ──/api──> Spring Boot (:8080) ──> MySQL (:3306)
              └── serves index.html, js/, css/
```

Only Nginx is exposed to the outside. Because the page and the API share one origin, no CORS is needed
in the Docker setup.

## Project structure

```
shelfie/
├── src/main/java/org/example/shelfie/
│   ├── ShelfieApplication.java
│   ├── book/       Book entity, repository, service, DTO, controller, seeder
│   └── common/     shared configuration (CORS for dev mode)
├── src/test/       tests + H2 test configuration
├── frontend/       index.html, js/, css/, nginx.conf, Dockerfile
├── Dockerfile      backend image (multi-stage Maven build)
├── compose.yaml    the full stack
└── .env.example    required environment variables
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
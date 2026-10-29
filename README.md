# Shelfie

A personal book tracker built to practise the topics from 3rd semester at EK:
a Spring Boot REST API, JPA, a plain JavaScript frontend, Docker, GitHub Actions and cloud deployment.

## Status

**v0.3.0 – Domain, relations and Streams**

- Users, shelves, shelf entries, books and reviews, mapped with JPA relations
- Read endpoints for books (with reviews and average rating), shelves, shelf entries and reading statistics
- All responses are DTOs (Java records), so no entity is serialized directly
- The whole stack runs with Docker Compose: Nginx (frontend + reverse proxy), Spring Boot and MySQL

Until login is added (Spring Security, phase 8), the app uses one seeded test user.

## Tech stack

| Part | Technology |
| --- | --- |
| Backend | Java 25, Spring Boot 4.1, Spring Web, Spring Data JPA, Maven |
| Database | MySQL 8.4 in Docker (H2 in-memory for tests) |
| Frontend | HTML, plain JavaScript (ES modules), Bootstrap 5, served by Nginx |
| Infrastructure | Docker (multi-stage builds), Docker Compose |
| Testing | JUnit, AssertJ, `@SpringBootTest`, `@DataJpaTest` |

## Run with Docker (full stack)

1. Copy `.env.example` to `.env` and fill in the passwords.
2. Start everything:
   ```
   docker compose up -d --build
   ```
3. Open http://localhost

Stop it with `docker compose down`. The database data is kept in the `db-data` volume
(`docker compose down -v` deletes it, and the seeders recreate the test data on the next start).

## Run in dev mode

Faster for day-to-day coding: only MySQL runs in Docker, the backend runs from IntelliJ.

1. Start the database: `docker compose up -d db`
2. Run `ShelfieApplication` in IntelliJ. It connects to MySQL on `localhost:3306`.
3. Open `frontend/index.html` in IntelliJ and click a browser icon (opens on `http://localhost:63342`,
   allowed by the CORS configuration).

Example requests for every endpoint are in `http/api.http` (run them from IntelliJ).

Run the tests with `.\mvnw test`. Tests use an in-memory H2 database, so MySQL doesn't need to be running.

## Architecture

```
Browser ──> Nginx (:80) ──/api──> Spring Boot (:8080) ──> MySQL (:3306)
              └── serves index.html, js/, css/
```

Only Nginx is exposed to the outside. Because the page and the API share one origin, no CORS is needed
in the Docker setup.

## Domain model

| Entity | Relations |
| --- | --- |
| `AppUser` | – (referenced by `Shelf` and `Review`) |
| `Shelf` | many-to-one `AppUser` (owner), one-to-many `ShelfEntry` (bidirectional, cascade) |
| `ShelfEntry` | many-to-one `Shelf`, many-to-one `Book`; unique per shelf and book |
| `Book` | one-to-many `Review` (bidirectional) |
| `Review` | many-to-one `Book`, many-to-one `AppUser` (author); one review per user per book |

`ShelfEntry` is a join entity instead of a `@ManyToMany`, so it can carry its own data
(`addedAt`, `finishedAt`).

## Project structure

```
shelfie/
├── src/main/java/org/example/shelfie/
│   ├── ShelfieApplication.java
│   ├── auth/       AppUser, CurrentUserService (seeded test user until phase 8)
│   ├── book/       Book entity, service, DTOs, controller, seeder
│   ├── shelf/      Shelf, ShelfEntry, ShelfType, service, DTOs, controller, seeder
│   ├── review/     Review entity, service, seeder
│   ├── stats/      reading statistics computed with Java Streams
│   └── common/     shared configuration (CORS for dev mode)
├── src/test/       tests + H2 test configuration
├── frontend/       index.html, js/, css/, nginx.conf, Dockerfile
├── http/           example requests for IntelliJ's HTTP client
├── Dockerfile      backend image (multi-stage Maven build)
├── compose.yaml    the full stack
└── .env.example    required environment variables
```

The backend is organised **package by feature**. Repositories are package-private, so features
only talk to each other through their public services.

## API

| Method | Path | Response |
| --- | --- | --- |
| GET | `/api/books` | `200` – all books |
| GET | `/api/books/{id}` | `200` – a book with its reviews and average rating, `404` if not found |
| GET | `/api/shelves` | `200` – my shelves with entry counts |
| GET | `/api/shelves/{id}/entries` | `200` – the books on one of my shelves, `404` if not found or not mine |
| GET | `/api/stats/me` | `200` – my books per shelf type, average rating given and top authors |

## Workflow

- `main` is protected: all changes go through a feature branch and a pull request
- Commit messages follow [Conventional Commits](https://www.conventionalcommits.org/) (`feat:`, `fix:`, `docs:`, `test:`, `chore:`)
- Each finished phase is tagged with a semantic version (`v0.1.0`, `v0.2.0`, …) and published as a GitHub Release
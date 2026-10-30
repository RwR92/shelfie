# Shelfie

A personal book tracker built to practise the topics from 3rd semester at EK:
a Spring Boot REST API, JPA, a plain JavaScript frontend, Docker, GitHub Actions and cloud deployment.

## Status

**v0.4.0 – Integration, errors and validation**

- Search and import real books from the [Open Library API](https://openlibrary.org/developers/api) with `RestClient`
- Put books on shelves, move them between shelves, remove them, and write reviews
- Input is validated with Bean Validation, and every error is returned as a `ProblemDetail`
- Users, shelves, shelf entries, books and reviews, mapped with JPA relations
- All responses are DTOs (Java records), so no entity is serialized directly
- The whole stack runs with Docker Compose: Nginx (frontend + reverse proxy), Spring Boot and MySQL

Until login is added (Spring Security, phase 8), the app uses one seeded test user.

## Tech stack

| Part | Technology |
| --- | --- |
| Backend | Java 25, Spring Boot 4.1, Spring Web, Spring Data JPA, Bean Validation, Maven |
| Integration | `RestClient` with timeouts, calling the Open Library API |
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

Example requests for every endpoint are in the `http/` folder, one file per feature.
In IntelliJ, pick the `dev` environment (backend on port 8080) or `docker` (through Nginx on port 80)
in the "Run with" dropdown at the top of the file.

Run the tests with `.\mvnw test`. Tests use an in-memory H2 database, so MySQL doesn't need to be running,
and they never call Open Library.

## Architecture

```
Browser ──> Nginx (:80) ──/api──> Spring Boot (:8080) ──> MySQL (:3306)
              └── serves index.html, js/, css/      └──> Open Library API (internet)
```

Only Nginx is exposed to the outside. Because the page and the API share one origin, no CORS is needed
in the Docker setup. Calls to Open Library have connect and read timeouts, and a failure is returned
as `502 Bad Gateway` instead of a `500`.

## Domain model

| Entity | Relations |
| --- | --- |
| `AppUser` | – (referenced by `Shelf` and `Review`) |
| `Shelf` | many-to-one `AppUser` (owner), one-to-many `ShelfEntry` (bidirectional, cascade, orphan removal) |
| `ShelfEntry` | many-to-one `Shelf`, many-to-one `Book`; unique per shelf and book |
| `Book` | one-to-many `Review` (bidirectional); optional unique `openLibraryKey` for imported books |
| `Review` | many-to-one `Book`, many-to-one `AppUser` (author); one review per user per book |

`ShelfEntry` is a join entity instead of a `@ManyToMany`, so it can carry its own data
(`addedAt`, `finishedAt`). A book on a `READ` shelf always has a `finishedAt` date: the rule lives in
the entity itself, in its constructor and in `moveTo`.

## Project structure

```
shelfie/
├── src/main/java/org/example/shelfie/
│   ├── ShelfieApplication.java
│   ├── auth/       AppUser, CurrentUserService (seeded test user until phase 8)
│   ├── book/       Book, service, DTOs, controller, seeder, Open Library client
│   ├── shelf/      Shelf, ShelfEntry, ShelfType, service, DTOs, controllers, seeder
│   ├── review/     Review, service, DTOs, controller, seeder
│   ├── stats/      reading statistics computed with Java Streams
│   └── common/     global exception handler, shared exceptions, CORS for dev mode
├── src/test/       tests + H2 test configuration
├── frontend/       index.html, js/, css/, nginx.conf, Dockerfile
├── http/           example requests per feature + environments (dev, docker)
├── Dockerfile      backend image (multi-stage Maven build)
├── compose.yaml    the full stack
└── .env.example    required environment variables
```

The backend is organised **package by feature**. Repositories are package-private, so features
only talk to each other through their public services.

## API

| Method | Path | Body | Response |
| --- | --- | --- | --- |
| GET | `/api/books` | – | `200` all books |
| GET | `/api/books/{id}` | – | `200` a book with its reviews and average rating · `404` |
| GET | `/api/books/search?q=` | – | `200` search results from Open Library · `400` without `q` · `502` if Open Library is down |
| POST | `/api/books/import/{olKey}` | – | `201` imported · `200` already imported · `404` unknown key · `502` |
| POST | `/api/books/{bookId}/reviews` | `{ "rating": 1-5, "text": "..." }` | `201` · `400` invalid · `404` · `409` already reviewed |
| GET | `/api/shelves` | – | `200` my shelves with entry counts |
| GET | `/api/shelves/{id}/entries` | – | `200` the books on one of my shelves · `404` |
| POST | `/api/shelves/{id}/entries` | `{ "bookId": 1 }` | `201` · `400` · `404` · `409` already on the shelf |
| PATCH | `/api/entries/{id}` | `{ "shelfId": 2 }` | `200` moved (moving to a Read shelf sets `finishedAt`) · `404` · `409` |
| DELETE | `/api/entries/{id}` | – | `204` · `404` |
| GET | `/api/stats/me` | – | `200` my books per shelf type, average rating given and top authors |

### Errors

All errors use the [RFC 9457](https://www.rfc-editor.org/rfc/rfc9457) `ProblemDetail` format
(`Content-Type: application/problem+json`). Validation errors also list the problem per field:

```json
{
  "status": 400,
  "title": "Bad Request",
  "detail": "Validation failed",
  "instance": "/api/books/5/reviews",
  "errors": { "rating": "must be less than or equal to 5" }
}
```

## Workflow

- `main` is protected: all changes go through a feature branch and a pull request
- Commit messages follow [Conventional Commits](https://www.conventionalcommits.org/) (`feat:`, `fix:`, `docs:`, `test:`, `chore:`)
- Each finished phase is tagged with a semantic version (`v0.1.0`, `v0.2.0`, …) and published as a GitHub Release
# URL Shortener

## Features

- Generates 6-character alphanumeric slugs (A–Z, a–z, 0–9)
- The same long URL always returns the exact same slug
- Collision-safe slug generation (checks PostgreSQL before saving)
- Two-level storage:
  - PostgreSQL - persistent storage
  - Redis - fast cache + warm-up on first redirect
- 302 redirects
- Dockerized (multi-stage build + docker-compose)

## Tech Stack

- Java 24
- Spring Boot 4.0.0
- Spring Data JPA + Spring Data Redis
- PostgreSQL 15
- Redis 7
- Flyway migrations
- Lombok
- Gradle
- Docker & Docker Compose

## How It Works (Important Details)

- Duplicate protection: before creating a new short link, the service checks `findByUrl(longUrl)` in Redis - if exists - returns cached result.
- Slug collision handling: slug is generated randomly, then checked via `findBySlug()` in PostgreSQL. Loop continues until a free slug is found.
- Same ID for JPA and Redis entities - consistent data across layers.

## API Endpoints

### 1. Create short link

```http
POST http://localhost:8087/api/v1/generate-slug
Content-Type: application/json
{
  "url": "https://start.spring.io/"
}
```

Response
```
{
  "id": "a1b2c3d4-e5f6-7890-g1h2-i3j4k5l6m7n8",
  "url": "https://start.spring.io/",
  "slug": "XyZ9kP"
}
```

### 2. Redirect
`GET http://localhost:8087/{slug}`

### How to Run
```# 1. Clone repo
git clone https://github.com/yourname/url-shortener.git
cd url-shortener

# 2. Start everything (Postgres + Redis + app)
docker-compose up --build
```

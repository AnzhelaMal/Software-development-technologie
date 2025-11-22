# Shell File Manager Web (TRPZ Demo)

- **Stack:** Spring Boot 3, Thymeleaf, Spring Data JPA, H2/PostgreSQL, Repository pattern
- **Forms:** (1) Login/Register, (2) Search (creates SearchQuery, scans local FS, persists results)
- **DB:** JPA entities `User`, `SearchQuery`, `SearchResult`

## Quick Start (H2, no external DB)
```bash
# Java 17 + Maven required
mvn spring-boot:run
# open http://localhost:8081/login
```
Create an account on /register, then go to /search, create a search, results are persisted and shown.

## PostgreSQL profile
Create DB `shellfmdb` and run:
```bash
# set credentials in src/main/resources/application-postgres.properties
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```
The schema is created automatically (Hibernate `ddl-auto=update`).

## Repository Pattern
- `IRepository<T,ID>` — base interface (didactic).
- JPA repositories: `UserRepository`, `SearchQueryRepository`, `SearchResultRepository` encapsulate DB access.
- Service layer (`AuthService`, `SearchService`) uses repositories — matches your component diagrams.

## Notes
- SearchService performs a **demo** scan under the server's `user.home` with a cap of 1000 items.
- Switch to real FS indexer or remote services per your SOA design later.

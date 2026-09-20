# bank-service-springboot

The same domain as the plain-Java version (`BankService`), reimplemented with **Spring Boot** — intended for comparison: how much infrastructure code the framework takes over compared to a hand-rolled implementation on servlets and JDBC.

## Stack

- Spring Boot (Spring Web, Spring Data JPA)
- PostgreSQL
- Maven

## How this differs from the plain-Java version

| Layer | Plain Java (`BankService`) | Spring Boot |
|---|---|---|
| Server startup | Manual `Server`/`ServletContextHandler`/`ServletHolder` setup | `@SpringBootApplication`, embedded Tomcat starts automatically |
| HTTP layer | `HttpServlet`, manual path and JSON parsing | `@RestController`, `@GetMapping`/`@PostMapping`/`@PutMapping`, `@PathVariable`, `@RequestBody` |
| Database access | Plain JDBC (`DriverManager`, `PreparedStatement`, manual `ResultSet` mapping) | Spring Data JPA — `interface AccountRepository extends JpaRepository<Account, Long>` with no SQL at all |
| Data model | Plain POJO | `@Entity` — the class itself describes the table mapping (`@Table`, `@Column`, `@Id`, `@GeneratedValue`) |
| Updating data | Explicit `UPDATE` in SQL | Change the object's field + `save()` — Hibernate figures out what to persist (dirty checking) |
| Error handling | `try/catch` in every servlet method | `@RestControllerAdvice` + `@ExceptionHandler` — centralized across the whole app |
| Configuration | `System.getenv(...)` | `application.properties` |

The business logic (sufficient-funds checks, before/after balance calculation, thread safety) is deliberately identical in meaning between the two versions, so the comparison highlights the difference in infrastructure code rather than business rules.

## Architecture

```
entity/     → Account — JPA entity
repository/ → AccountRepository — interface, extends JpaRepository
service/    → AccountService / AccountServiceImpl — business logic, @Service
controller/ → AccountController — @RestController
dto/        → AmountRequest, BalanceChangeResponse
exceptions/ → AccountNotFoundException, InsufficientFoundsException, GlobalExceptionHandler
```

## Configuration

`src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/bankdb
spring.datasource.username=postgres
spring.datasource.password=123
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

`ddl-auto=update` — Hibernate creates/updates the table schema from the entity classes automatically on startup.

## Running

```bash
mvn spring-boot:run
```

or run the main class annotated with `@SpringBootApplication` from your IDE.

The server starts on port `8080`.

## REST API

| Method | Path                        | Description               |
|--------|------------------------------|-----------------------------|
| GET    | `/accounts/{id}`              | Get an account              |
| POST   | `/accounts`                    | Create an account           |
| PUT    | `/accounts/{id}/deposit`      | Deposit into an account     |
| PUT    | `/accounts/{id}/withdraw`     | Withdraw from an account    |

### Example

```bash
curl -X GET http://localhost:8080/accounts/1

curl -X PUT http://localhost:8080/accounts/1/deposit \
  -H "Content-Type: application/json" \
  -d '{"amount": 100}'
```

## Thread safety

The `deposit`/`withdraw` methods in `AccountServiceImpl` are synchronized (`synchronized`), matching the plain-Java version.

## Error handling

Exceptions (`AccountNotFoundException` → 404, `InsufficientFoundsException` → 400, `IllegalArgumentException` → 400) are caught centrally in `GlobalExceptionHandler` (`@RestControllerAdvice`), avoiding duplicated try/catch blocks in every controller method.

## Building with Docker

```bash
docker build -t bank-service-springboot .
```

`spring-boot-maven-plugin` (included by default in Spring Boot projects) builds the executable jar on its own — a separate `maven-shade-plugin`, as used in the plain-Java version, is not needed.

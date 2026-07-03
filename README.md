# ComplexoSantosScheinBackend

Backend Java com Spring Boot para o aplicativo Android do Complexo Esportivo Santos e Schein.

## Stack inicial

- Java 21
- Spring Boot
- Gradle
- Spring Web
- Spring Security
- Spring Data JPA
- Oracle JDBC Driver
- Flyway
- Bean Validation
- JWT
- JUnit 5
- Mockito

## Estrutura principal

```text
src/main/java/br/com/santosschein/complexo
├── domain
├── application
├── infrastructure
├── interfaces
├── security
└── shared/exception
```

## Subir o Oracle local

Na raiz do projeto:

```powershell
docker compose up -d
```

O banco fica disponível em:

```text
jdbc:oracle:thin:@//localhost:1521/FREEPDB1
user: complexo
password: complexo
```

As tabelas são criadas exclusivamente pelas migrations do Flyway em:

```text
src/main/resources/db/migration
```

## Executar a aplicação

Com o Oracle já iniciado:

```powershell
.\gradlew.bat bootRun
```

A API sobe em:

```text
http://localhost:8080/
```

Para o emulador Android, use:

```text
http://10.0.2.2:8080/
```

## Rodar os testes

```powershell
.\gradlew.bat test
```

Nesta etapa inicial os testes são unitários e não dependem do Oracle.

## Testar no Postman ou Insomnia

Endpoint técnico inicial:

```http
GET http://localhost:8080/api/health
```

Endpoints planejados para as próximas etapas:

```http
POST /api/auth/register
POST /api/auth/login
GET  /api/users/me
GET  /api/reservations/me/next
GET  /api/reservations/available-days?year=2026&month=7
GET  /api/reservations/available-times?date=2026-07-10
POST /api/reservations
GET  /api/reservations/me?type=UPCOMING
PATCH /api/reservations/{reservationId}/cancel
```

Login e registro serão públicos. Os demais endpoints serão protegidos por JWT no header:

```http
Authorization: Bearer <token>
```

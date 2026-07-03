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
- Flyway via Docker
- Bean Validation
- JWT
- Lombok
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
docker compose up oracle flyway
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

O Flyway roda em um container dedicado e monta esse diretório como `/flyway/sql`.
A aplicação Spring Boot não executa migrations ao iniciar; ela apenas valida o schema com:

```properties
spring.jpa.hibernate.ddl-auto=validate
```

Para o container Flyway acessar Oracle, coloque o driver JDBC em:

```text
docker/flyway/drivers/ojdbc11.jar
```

Novas alterações de banco devem ser adicionadas como novos arquivos `V__*.sql`.

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
GET http://localhost:8080/api/v1/health
```

Endpoints principais:

```http
POST   /api/v1/auth/register
POST   /api/v1/auth/login
GET    /api/v1/users/me
GET    /api/v1/reservations/available-days?year=2026&month=7
GET    /api/v1/reservations/available-slots?date=2026-07-10
POST   /api/v1/reservations
GET    /api/v1/users/me/reservations/next
GET    /api/v1/users/me/reservations?status=upcoming
GET    /api/v1/users/me/reservations?status=past
DELETE /api/v1/users/me/reservations/{reservationId}
GET    /api/v1/health
```

Login e registro são públicos. Os demais endpoints são protegidos por JWT no header:

```http
Authorization: Bearer <token>
```

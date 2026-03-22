# Environment Variables

Este documento descreve as variaveis de ambiente usadas no desenvolvimento local.

## Como funciona no projeto

- O `docker compose` le variaveis do arquivo `.env` automaticamente para interpolar o `docker-compose.yml`.
- O Spring Boot importa o arquivo `.env` automaticamente via `spring.config.import` em `src/main/resources/application.yaml`.
- O Spring Boot tambem continua aceitando variaveis do ambiente do processo (shell), que podem sobrescrever valores do arquivo.
- O profile de desenvolvimento usa `src/main/resources/application-dev.yaml`.

## Variaveis usadas pelo Spring Boot (profile dev)

Arquivo relacionado: `src/main/resources/application-dev.yaml`.

- `DB_URL`: URL JDBC completa. Se definida, tem prioridade sobre host/porta/nome.
- `DB_HOST`: host do banco (padrao: `localhost`).
- `DB_PORT`: porta do banco (padrao: `5432`).
- `DB_NAME`: nome do banco (padrao: `medflow`).
- `DB_SCHEMA`: schema usado pelo Hibernate/Flyway (padrao: `medflow`).
- `DB_USERNAME`: usuario do banco (padrao: `medflow`).
- `DB_PASSWORD`: senha do banco (padrao: `medflow`).

## Variaveis usadas pelo Docker Compose

Arquivo relacionado: `docker-compose.yml`.

- `POSTGRES_VERSION`: tag da imagem Postgres (padrao: `18`).
- `POSTGRES_CONTAINER_NAME`: nome do container (padrao: `medflow-postgres`).
- `TZ`: timezone do container (padrao: `America/Belem`).
- `DB_PORT`: publica a porta local do banco (padrao: `5432`).
- `DB_NAME`: nome do banco criado no Postgres (padrao: `medflow`).
- `DB_USERNAME`: usuario do Postgres (padrao: `medflow`).
- `DB_PASSWORD`: senha do Postgres (padrao: `medflow`).

## Exemplo de `.env` local

```env
spring.profiles.active=dev

DB_HOST=localhost
DB_PORT=5432
DB_NAME=medflow
DB_SCHEMA=medflow
DB_USERNAME=medflow
DB_PASSWORD=medflow

# Opcional: sobrescreve host/porta/nome no Spring
# DB_URL=jdbc:postgresql://localhost:5432/medflow

POSTGRES_VERSION=18
POSTGRES_CONTAINER_NAME=medflow-postgres
TZ=America/Belem
```

## Fluxo de desenvolvimento local

1. Criar/ajustar o arquivo `.env` na raiz.
2. Subir banco: `docker compose up -d`.
3. Subir app (sem exportar envs no shell):

```bash
./mvnw spring-boot:run
```

4. Alternativa para sobrescrever profile manualmente:

```bash
SPRING_PROFILES_ACTIVE=dev ./mvnw spring-boot:run
```

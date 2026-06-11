**Medflow** é um sistema de gestão clínica projetado para otimizar o fluxo de agendamentos, atendimentos e gestão administrativa de clínicas e consultórios médicos. O sistema foca na separação clara entre a gestão de identidade/autenticação (via Keycloak) e as regras de negócio clínicas.

## 🚀 Funcionalidades Principais

O sistema atende a diferentes perfis de usuários com funcionalidades específicas:

- **Paciente (Usuário Comum):** Consulta de especialidades e médicos, agendamento de consultas presenciais, acompanhamento de status de atendimento e acesso ao seu histórico clínico.
- **Médico:** Gestão de agenda, controle de fila de atendimentos e registro de informações clínicas (queixa principal, anamnese, conduta).
- **Recepcionista:** Gestão operacional da clínica, check-in de pacientes, reagendamentos e cadastro de usuários.
- **Administrador:** Gestão completa da organização, incluindo unidades, consultórios, alocação de médicos, especialidades e permissões de acesso.

## 🛠️ Arquitetura Técnica

O Medflow utiliza uma arquitetura moderna e robusta:

### Backend

- **Linguagem:** Java 25
- **Framework:** Spring Boot 4.0.6
- **Banco de Dados:** PostgreSQL 18 (com migrações via Flyway)
- **Segurança:** OAuth2 / OpenID Connect integrado ao **Keycloak**
- **Auditoria:** Hibernate Envers para versionamento de entidades
- **API:** REST com documentação via OpenAPI (Swagger)

### Frontend

- **Framework:** Angular 21
- **Estilização:** Tailwind CSS & PrimeNG
- **Autenticação:** `keycloak-angular`

### Infraestrutura (Docker)

O projeto conta com um ambiente containerizado facilitando o desenvolvimento:

- **Postgres:** Banco de dados relacional.
- **Keycloak:** Servidor de Identidade e Acesso (IAM) para autenticação e autorização funcional.

## 🔐 Modelo de Segurança

O sistema implementa dois níveis de autorização:

1.  **Autorização Funcional:** Gerenciada pelo Keycloak (ex: "O usuário pode criar consultas?").
2.  **Autorização Contextual:** Gerenciada pelo Backend do Medflow (ex: "Este médico é o responsável por esta consulta específica?").

## 🛠️ Como Executar

### Pré-requisitos

- Docker e Docker Compose
- Java 25 (para execução local do backend)
- Node.js (para execução local do frontend)

### Iniciando a Infraestrutura

```bash
docker-compose up -d
```

### Executando o Backend

```bash
./mvnw spring-boot:run
```

### Executando o Frontend

```bash
npm install
npm start
```

## 📚 Documentação Adicional

Para mais detalhes sobre a arquitetura, requisitos funcionais e não funcionais, consulte a pasta `wiki` do repositório.

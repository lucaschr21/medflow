# Qualidade de codigo

## Frontend (Angular)

- ESLint com regras para Angular e templates HTML.
- Stylelint para SCSS/CSS.
- Prettier para formatacao.
- Husky + lint-staged + commitlint para validacao em commits.

Comandos principais:

```bash
npm run lint
npm run lint:fix
npm run stylelint
npm run stylelint:fix
npm run format
npm run format:check
npm run quality
```

## Backend (Spring Boot / Maven)

Plugins adicionados no Maven:

- Checkstyle
- PMD
- SpotBugs
- OWASP Dependency-Check

Comandos principais:

```bash
./mvnw verify
./mvnw checkstyle:check
./mvnw pmd:check
./mvnw spotbugs:check
./mvnw dependency-check:check
```

Execucao rapida (sem scan de dependencias):

```bash
./mvnw verify -Ddependency-check.skip=true
```

# Contexto Geral do Projeto Medflow

## 1. Visão do produto

O Medflow é uma aplicação web de gestão clínica para organizações de saúde. O objetivo do sistema é permitir que uma clínica ou organização gerencie usuários, médicos, especialidades, unidades, consultórios, agendas, consultas, registros de atendimento, anexos e relatórios administrativos.

O foco principal do produto não é ser apenas um CRUD genérico de entidades. O Medflow deve funcionar como um sistema operacional clínico, com fluxos reais para:

* usuário comum agendar e acompanhar consultas;
* recepcionista operar a agenda do dia;
* médico atender sua fila e registrar atendimentos;
* administrador configurar e supervisionar a organização.

A interface e o backend devem ser guiados pelos fluxos do produto, não apenas pelas entidades.

---

## 2. Stack e arquitetura geral

### Backend

O backend é em Spring Boot, com Java, JPA/Hibernate, PostgreSQL e integração com Keycloak.

Responsabilidades principais do backend:

* validar autenticação via Keycloak;
* consultar autorização funcional no Keycloak Authorization Services;
* aplicar regras contextuais do domínio;
* persistir dados do Medflow;
* controlar fluxo de consultas e atendimentos;
* futuramente criar/atualizar/desativar usuários no Keycloak pela interface da aplicação.

### Frontend

O frontend será Angular, com PrimeNG, mas atualmente também existe um protótipo visual navegável em React + Tailwind para auxiliar no design system e layout.

Responsabilidades principais do frontend:

* autenticar via Keycloak;
* carregar permissões funcionais do Keycloak para controle visual;
* adaptar a interface conforme o tipo de usuário;
* esconder/desabilitar menus, botões e ações conforme permissões;
* consumir endpoints operacionais do backend;
* representar corretamente os fluxos de consulta, recepção, atendimento e administração.

### Keycloak

O Keycloak é usado para:

* autenticação;
* sessão;
* credenciais;
* roles/grupos;
* permissões funcionais via Authorization Services.

O backend Medflow não armazena senha. Ele mantém apenas o vínculo local com o usuário do Keycloak por meio do `keycloakId`.

---

## 3. Modelo de autorização decidido

O projeto usa um modelo híbrido:

```text
Role/grupo
→ define a experiência principal da interface.

Permission resource:scope
→ define ações, menus, botões e rotas funcionais.

Regra contextual no backend
→ define quais dados o usuário pode acessar ou modificar.
```

Exemplo:

```text
Role MEDICO
→ renderiza experiência médica.

Permission consulta:update
→ permite mostrar ações como iniciar atendimento.

Regra contextual
→ médico só atua em consultas atribuídas a ele.
```

Isso evita usar somente roles para tudo e também evita usar somente permissions para definir experiência de usuário.

---

## 4. Roles/grupos principais

Os tipos principais de usuário são:

* `ADMINISTRADOR`
* `RECEPCIONISTA`
* `MEDICO`
* `USUARIO`

A interface deve mudar conforme o perfil operacional:

### Usuário comum

Foco:

* agendar consulta;
* ver próximas consultas;
* ver histórico;
* visualizar anexos próprios.

### Médico

Foco:

* ver agenda do dia;
* ver fila de atendimento;
* iniciar/continuar atendimento;
* registrar atendimento;
* finalizar atendimento.

O médico não deve ter uma experiência centrada em “agendar consulta”. Ele trabalha com consultas atribuídas a ele.

### Recepcionista

Foco:

* operar agenda do dia;
* fazer check-in;
* acompanhar fila;
* reagendar/cancelar consultas;
* cadastrar usuários comuns;
* anexar arquivos.

### Administrador

Foco:

* configurar organização;
* cadastrar unidades, consultórios, usuários, médicos e especialidades;
* configurar alocação e agenda médica;
* supervisionar relatórios.

---

## 5. Decisão sobre permissões funcionais

Foi decidido seguir uma abordagem simples para o MVP: manter os resources atuais por entidade e permitir que alguns perfis tenham `read` operacional de recursos necessários ao fluxo.

Exemplo:

Um usuário comum precisa conseguir ver médicos, especialidades e unidades disponíveis para agendar uma consulta. Isso exige permissões como:

```text
medico:read
especialidade:read
unidade:read
```

Isso não significa que ele pode acessar a tela administrativa de médicos, especialidades ou unidades.

A regra é:

```text
read não significa automaticamente acesso ao CRUD administrativo.
read significa leitura funcional daquele recurso.

A experiência da UI e as regras contextuais do backend limitam onde e como essa leitura aparece.
```

Então:

```text
USUARIO pode ter medico:read
```

mas:

* ele não vê menu administrativo “Médicos”;
* ele só vê médicos dentro do fluxo de agendamento;
* o backend filtra e expõe apenas dados compatíveis com aquele fluxo.

---

## 6. Matriz inicial de permissões por perfil

### USUARIO

Permissões mínimas para o usuário comum conseguir usar o sistema:

```text
especialidade:read
unidade:read
medico:read
consulta:create
consulta:read
consulta:update
anexo-consulta:read
```

Uso:

* consultar especialidades;
* consultar unidades;
* consultar médicos disponíveis;
* agendar consulta;
* ver suas próprias consultas;
* cancelar ou solicitar reagendamento quando permitido;
* visualizar anexos das próprias consultas.

### MEDICO

Permissões mínimas:

```text
consulta:read
consulta:update
registro-atendimento:create
registro-atendimento:read
registro-atendimento:update
anexo-consulta:read
bloqueio-agenda:create
bloqueio-agenda:read
bloqueio-agenda:delete
```

Uso:

* ver própria agenda;
* ver fila;
* iniciar atendimento;
* registrar atendimento;
* finalizar atendimento;
* visualizar anexos;
* cadastrar bloqueios, se permitido.

### RECEPCIONISTA

Permissões mínimas:

```text
usuario:create
usuario:read
consulta:read
consulta:create
consulta:update
medico:read
especialidade:read
unidade:read
consultorio:read
agenda:read
anexo-consulta:create
anexo-consulta:read
anexo-consulta:delete
```

Uso:

* cadastrar usuário comum;
* operar agenda;
* fazer check-in;
* reagendar/cancelar consultas;
* marcar não comparecimento;
* anexar arquivos;
* acompanhar fila.

### ADMINISTRADOR

Permissões amplas de gestão:

```text
organizacao:read
organizacao:update

usuario:create
usuario:read
usuario:update
usuario:deactivate

unidade:create
unidade:read
unidade:update
unidade:deactivate

consultorio:create
consultorio:read
consultorio:update
consultorio:deactivate

especialidade:create
especialidade:read
especialidade:update
especialidade:deactivate

medico:create
medico:read
medico:update
medico:deactivate

alocacao-medico:create
alocacao-medico:read
alocacao-medico:update
alocacao-medico:deactivate

agenda:create
agenda:read
agenda:update
agenda:deactivate

bloqueio-agenda:create
bloqueio-agenda:read
bloqueio-agenda:update
bloqueio-agenda:delete

consulta:read
consulta:update

relatorio:read
```

---

## 7. Resources e scopes

O padrão de autorização funcional é:

```text
resource:scope
```

Scopes principais:

```text
create
read
update
delete
deactivate
```

Resources principais:

```text
organizacao
usuario
unidade
consultorio
especialidade
medico
alocacao-medico
agenda
bloqueio-agenda
consulta
registro-atendimento
anexo-consulta
relatorio
```

---

## 8. Backend — o que já foi feito

Já foi implementada uma base robusta de segurança no backend:

### Autenticação

* backend configurado como OAuth2 Resource Server;
* uso de opaque token/introspection;
* principal autenticado tipado;
* extração de dados do usuário autenticado;
* leitura de claims customizadas como CPF, telefone e dataNascimento.

### Autorização funcional

Foi implementada a abstração:

```text
@AuthorizeResource
@AuthorizePermission
FunctionalAuthorizer
FunctionalPermissionDecisionService
KeycloakAuthorizationDecisionService
```

Funcionamento:

* `@AuthorizeResource(Consulta.class)` infere o resource pela classe;
* o método HTTP ou resolver interno define a action;
* o backend monta uma permissão no formato `consulta#create`;
* consulta o Keycloak Authorization Services com UMA;
* usa `response_mode=decision`;
* interpreta resposta `{"result": true|false}`;
* se o Keycloak negar, o backend nega;
* se o Keycloak permitir, a aplicação ainda aplica regras contextuais.

### Soft delete

Foi ajustada a ideia de `delete/deactivate`.

A aplicação usa `DELETE` no HTTP. O backend identifica se a entidade usa soft delete. Se usar soft delete, a ação funcional exigida é `deactivate`; se for exclusão física, a ação funcional exigida é `delete`.

Assim:

```text
DELETE em entidade soft delete
→ resource:deactivate

DELETE em entidade hard delete
→ resource:delete
```

### Frontend security

No frontend foi implementado:

* autenticação via Keycloak;
* carregamento de permissões via Keycloak Authorization Services;
* `response_mode=permissions`;
* normalização em `Map<Resource, Set<Scope>>`;
* `AuthorizationService.can(...)`;
* diretiva `mfCan`;
* guard de autorização;
* `ProtectedResourceService` para services de recurso;
* verificação visual de permissões.

---

## 9. Backend — o que falta para o fluxo funcionar

A seguir estão os blocos que ainda faltam ou precisam ser planejados/implementados para o backend sustentar o produto.

---

### 9.1 Ajustar permissões no Keycloak

Precisamos atualizar a configuração do Keycloak para a matriz de permissões definida.

Principalmente:

* dar ao usuário comum permissões operacionais de leitura:

  * `especialidade:read`
  * `unidade:read`
  * `medico:read`
  * `consulta:create/read/update`
  * `anexo-consulta:read`

Isso é necessário para que ele consiga agendar e acompanhar consultas.

Também revisar permissões de médico, recepcionista e administrador conforme a matriz definida.

---

### 9.2 Integração backend com Keycloak Admin API

Atualmente o backend ainda não cria usuários no Keycloak pela aplicação.

Isso é necessário para que a tela “Novo usuário” funcione.

O fluxo correto é:

```text
Frontend
→ POST /api/usuarios

Backend
→ valida usuario:create
→ valida regra contextual
→ cria usuário no Keycloak
→ atribui role/grupo
→ cria Usuario local no banco com keycloakId
→ retorna usuário criado
```

O frontend não deve chamar Keycloak Admin API diretamente.

É necessário criar uma integração backend com Keycloak Admin API.

Componentes sugeridos:

```text
IdentityProviderAdminPort
KeycloakIdentityProviderAdminAdapter
KeycloakAdminProperties
KeycloakAdminTokenClient
```

Operações necessárias:

```text
createUser
assignClientRole
joinGroup
disableUser
enableUser
updateUser
setTemporaryPassword
```

No Keycloak, criar um client técnico, por exemplo:

```text
medflow-admin-service
```

com service account habilitada e permissões administrativas limitadas.

---

### 9.3 Cadastro de usuário

Endpoint principal:

```http
POST /api/usuarios
```

Payload esperado:

```text
username
email
firstName
lastName
cpf
telefone
dataNascimento
tipoAcesso
```

Regras:

```text
ADMINISTRADOR pode criar:
- USUARIO
- RECEPCIONISTA
- MEDICO
- ADMINISTRADOR

RECEPCIONISTA pode criar:
- USUARIO

MEDICO não cria usuários.
USUARIO não cria usuários.
```

O backend deve criar o usuário no Keycloak e depois criar o vínculo local no Medflow.

---

### 9.4 Cadastro de médico

Médico depende de um usuário.

Fluxo recomendado:

```text
1. Criar usuário com tipo MEDICO.
2. Criar registro Medico local vinculado ao Usuario.
3. Vincular especialidades.
```

A interface pode parecer um fluxo único, mas internamente pode chamar endpoints separados.

Endpoints possíveis:

```http
POST /api/usuarios
POST /api/medicos
POST /api/medicos/{id}/especialidades
```

ou um endpoint composto no futuro.

---

### 9.5 Endpoints orientados por fluxo

Não basta CRUD puro. O produto precisa de endpoints operacionais.

#### Usuário comum

```http
GET  /api/consultas/minhas
POST /api/consultas
```

#### Agendamento

```http
GET /api/agendamento/opcoes
GET /api/agendamento/horarios-disponiveis
```

ou endpoints equivalentes para:

* especialidades disponíveis;
* unidades disponíveis;
* médicos disponíveis;
* horários disponíveis.

#### Recepção

```http
GET  /api/consultas/agenda-operacional
POST /api/consultas/{id}/check-in
POST /api/consultas/{id}/cancelar
POST /api/consultas/{id}/reagendar
POST /api/consultas/{id}/nao-compareceu
```

#### Médico

```http
GET  /api/consultas/minha-agenda
GET  /api/consultas/minha-fila
POST /api/consultas/{id}/iniciar-atendimento
POST /api/consultas/{id}/finalizar
```

#### Registro de atendimento

```http
POST /api/consultas/{id}/registro-atendimento
PUT  /api/consultas/{id}/registro-atendimento
```

#### Anexos

```http
GET    /api/consultas/{id}/anexos
POST   /api/consultas/{id}/anexos
GET    /api/anexos-consulta/{id}/download
DELETE /api/anexos-consulta/{id}
```

#### Relatórios

```http
GET /api/relatorios/resumo-consultas
```

---

### 9.6 Disponibilidade de agenda

Esse é um dos blocos mais importantes.

Para agendar consulta, o backend precisa calcular horários disponíveis considerando:

```text
médico ativo
especialidade ativa
médico vinculado à especialidade
unidade ativa
consultório ativo
alocação médica válida
agenda médica configurada
dia da semana
horário inicial/final
duração da consulta
bloqueios de agenda
consultas existentes
conflito de médico
conflito de consultório
```

A UI não deve calcular isso. Ela deve apenas exibir o que o backend retornar.

Criar um service:

```text
DisponibilidadeAgendaService
```

---

### 9.7 Transação crítica de agendamento

Ao confirmar uma consulta, o backend deve revalidar a disponibilidade.

Fluxo:

```text
1. Usuário escolhe horário.
2. Frontend envia criação de consulta.
3. Backend abre transação.
4. Backend revalida disponibilidade.
5. Se horário ainda estiver livre, cria consulta.
6. Se não estiver, retorna erro de conflito.
```

Erro esperado:

```text
HORARIO_INDISPONIVEL
```

HTTP sugerido:

```text
409 Conflict
```

---

### 9.8 Máquina de estados da consulta

O backend precisa controlar transições válidas.

Status:

```text
AGENDADA
CONFIRMADA
EM_ESPERA
EM_ATENDIMENTO
FINALIZADA
CANCELADA
NAO_COMPARECEU
```

Transições normais:

```text
AGENDADA -> CONFIRMADA
CONFIRMADA -> EM_ESPERA
EM_ESPERA -> EM_ATENDIMENTO
EM_ATENDIMENTO -> FINALIZADA
```

Alternativas:

```text
AGENDADA/CONFIRMADA -> CANCELADA
AGENDADA/CONFIRMADA/EM_ESPERA -> NAO_COMPARECEU
```

Bloquear transições inválidas, como:

```text
FINALIZADA -> EM_ATENDIMENTO
CANCELADA -> EM_ESPERA
NAO_COMPARECEU -> FINALIZADA
```

Criar um service tipo:

```text
ConsultaWorkflowService
ConsultaStatusService
```

---

### 9.9 Registro de atendimento

Regras:

```text
registro pertence a uma consulta
somente médico responsável pode registrar
consulta deve estar EM_ATENDIMENTO
ao finalizar, consulta vira FINALIZADA
registro pode ser criado/atualizado conforme regra
```

Campos:

```text
queixaPrincipal
anamnese
conduta
observacoes
```

---

### 9.10 Regras contextuais de acesso

Keycloak decide permissão funcional. Backend decide contexto.

Regras necessárias:

```text
usuário comum só vê suas próprias consultas
médico só vê consultas atribuídas a ele
recepcionista vê consultas da organização
administrador vê dados da organização
usuário inativo não opera
médico inativo não atende
entidades devem pertencer à mesma organização
consulta só pode mudar de status se transição for válida
```

Criar policies/services contextuais:

```text
UsuarioContextPolicy
OrganizacaoAccessPolicy
ConsultaAccessPolicy
MedicoAccessPolicy
```

---

### 9.11 Erros padronizados

O frontend precisa distinguir os erros.

Padronizar respostas como:

```json
{
  "code": "HORARIO_INDISPONIVEL",
  "message": "O horário selecionado não está mais disponível.",
  "details": {}
}
```

Códigos importantes:

```text
USER_NOT_LINKED
USER_INACTIVE
ORGANIZATION_INACTIVE
ACCESS_DENIED
HORARIO_INDISPONIVEL
TRANSICAO_CONSULTA_INVALIDA
MEDICO_INDISPONIVEL
CONSULTORIO_INDISPONIVEL
VALIDATION_ERROR
RESOURCE_NOT_FOUND
```

---

## 10. Frontend — o que já foi feito

Já foi planejado/implementado:

* autenticação via Keycloak;
* carregamento de permissões funcionais;
* controle visual com `mfCan`;
* guards de autorização;
* service de autorização;
* base de protected resource service;
* design system inicial planejado;
* layout principal planejado;
* sidebar/topbar planejadas;
* design adaptativo por usuário planejado.

---

## 11. Frontend — o que falta

### 11.1 Ajustar experiência por perfil

Frontend deve usar roles/grupos do token para definir experiência principal:

```text
ADMINISTRADOR -> experiência administrativa
MEDICO -> experiência médica
RECEPCIONISTA -> experiência recepção
USUARIO -> experiência usuário comum
```

Permissions controlam ações específicas.

Regra:

```text
role/grupo escolhe a experiência
permission escolhe ação
backend escolhe dados
```

---

### 11.2 Navegação adaptativa

Não usar uma sidebar única genérica.

Criar navegação por experiência:

```text
USUARIO:
- Início
- Agendar consulta
- Minhas consultas
- Histórico
- Anexos

MEDICO:
- Início
- Minha agenda
- Fila
- Atendimentos
- Bloqueios, se permitido

RECEPCIONISTA:
- Início
- Agenda operacional
- Fila
- Consultas
- Usuários comuns
- Anexos

ADMINISTRADOR:
- Início
- Organização
- Usuários
- Médicos
- Especialidades
- Unidades
- Consultórios
- Alocações médicas
- Agenda médica
- Bloqueios
- Relatórios
```

Cada item ainda deve ser filtrado por permission.

---

### 11.3 Home por perfil

Criar home diferente para cada experiência.

#### Home USUARIO

Mostrar:

```text
próxima consulta
botão Agendar consulta
histórico recente
status das consultas
anexos recentes
```

#### Home MEDICO

Mostrar:

```text
próximo atendimento
fila em espera
agenda do dia
botão Iniciar atendimento
botão Continuar atendimento
```

#### Home RECEPCIONISTA

Mostrar:

```text
agenda operacional do dia
consultas confirmadas
pacientes aguardando
check-in pendentes
fila de atendimento
```

#### Home ADMINISTRADOR

Mostrar:

```text
checklist de configuração inicial
atalhos de cadastros
indicadores simples
alertas de configuração incompleta
relatórios, se permitido
```

---

### 11.4 Fluxo de agendamento

Criar tela em etapas:

```text
1. Especialidade
2. Unidade
3. Médico
4. Data e horário
5. Motivo
6. Revisão e confirmação
```

Estados:

```text
carregando opções
sem especialidade
sem médico disponível
sem horário disponível
horário indisponível após confirmação
sucesso
erro
```

---

### 11.5 Agenda operacional da recepção

Criar tela operacional:

```text
filtro por data
filtro por unidade
filtro por consultório
filtro por médico
filtro por status
busca por usuário
tabela/board de consultas
ações rápidas
```

Ações:

```text
check-in
confirmar
cancelar
reagendar
marcar não compareceu
anexar arquivo
ver detalhes
```

---

### 11.6 Área médica

Criar telas:

```text
minha agenda
minha fila
detalhe da consulta
registro de atendimento
```

Ações:

```text
iniciar atendimento
continuar atendimento
salvar registro
finalizar atendimento
```

---

### 11.7 Administração

Criar telas administrativas com fluxo correto:

```text
organização
unidades
consultórios
especialidades
usuários
médicos
alocações médicas
agenda médica
bloqueios
relatórios
```

Mas a administração não deve ser o centro da experiência de todos. Ela é a experiência principal do administrador.

---

### 11.8 Estados importantes

Criar telas/estados para:

```text
sem permissão
sem dados
carregando
erro
configuração incompleta
usuário sem acesso
ação concluída
confirmação crítica
```

---

## 12. Prioridade para finalizar e apresentar

Para apresentação e MVP, priorizar:

### Backend

1. Ajustar matriz de permissões no Keycloak.
2. Implementar endpoints mínimos de consulta/agendamento.
3. Implementar disponibilidade de horários.
4. Implementar criação de consulta com revalidação.
5. Implementar transições de status.
6. Implementar agenda/fila do médico.
7. Implementar agenda operacional da recepção.
8. Implementar registro de atendimento.
9. Implementar criação de usuários via backend + Keycloak Admin API.
10. Implementar cadastro de médicos e especialidades.
11. Implementar erros padronizados.

### Frontend

1. Ajustar navegação por experiência.
2. Criar home por perfil.
3. Criar fluxo de agendamento.
4. Criar agenda operacional da recepção.
5. Criar área médica.
6. Criar registro de atendimento.
7. Criar checklist administrativo.
8. Criar cadastro de usuário/médico.
9. Criar estados de erro/vazio/sem permissão.
10. Aplicar `mfCan` e permissions nos menus e ações.

---

## 13. Diretriz principal

O Medflow não deve parecer um CRUD genérico.

A interface deve refletir jornadas reais:

```text
Administrador configura a clínica.
Usuário comum agenda consulta.
Recepcionista opera o dia.
Médico atende.
Usuário comum acompanha histórico.
```

A autorização deve ser apresentada como ponto forte:

```text
Keycloak centraliza a autorização funcional.
Backend valida o contexto real.
Frontend usa as permissões para adaptar visualmente a interface.
```

Essa separação é uma decisão arquitetural importante do projeto.

---

## 14. Escopo realista para apresentação

Para apresentar bem, não é necessário implementar tudo completo, mas é importante demonstrar o fluxo principal.

Fluxo mínimo de demonstração:

```text
1. Admin configura especialidade, médico e agenda.
2. Usuário comum agenda uma consulta.
3. Recepcionista faz check-in.
4. Médico inicia atendimento.
5. Médico registra e finaliza atendimento.
6. Usuário comum vê consulta no histórico.
```

Esse fluxo mostra:

* autenticação;
* autorização;
* permissões por perfil;
* regras contextuais;
* agenda;
* consulta;
* recepção;
* atendimento médico;
* registro;
* UI adaptativa.

Esse deve ser o foco do MVP.

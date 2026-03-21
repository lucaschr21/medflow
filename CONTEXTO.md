O Medflow é um sistema de gestão clínica e prontuário eletrónico desenvolvido para otimizar o fluxo de atendimento em clínicas médicas, centralizando processos administrativos, operacionais e clínicos em uma única plataforma.

O objetivo principal do sistema é melhorar a eficiência no agendamento de consultas, no atendimento ao paciente e no registo de informações clínicas, garantindo organização, segurança e facilidade de acesso aos dados. A aplicação é projetada para ser utilizada por três perfis principais de utilizadores: pacientes, médicos e rececionistas/administradores da clínica.

Para os pacientes, o sistema oferece funcionalidades de autoatendimento, permitindo o agendamento e gestão de consultas, acesso ao histórico clínico e visualização de exames e documentos. Para os médicos, o foco está na agilidade durante o atendimento, disponibilizando acesso rápido ao prontuário do paciente, histórico clínico e ferramentas para registo da consulta e emissão de documentos médicos. Já para os rececionistas e administradores, o sistema fornece mecanismos de controlo do fluxo de atendimento, gestão de profissionais, organização da agenda e acompanhamento financeiro da clínica.

O Medflow também contempla requisitos essenciais de segurança e privacidade, garantindo que os dados sensíveis dos pacientes sejam protegidos através de controlo de acesso, registos de auditoria e boas práticas de proteção de dados. Além disso, o sistema é projetado para ser escalável, confiável e de fácil utilização, assegurando uma experiência eficiente para todos os utilizadores.

De forma geral, o Medflow visa digitalizar e otimizar o ciclo completo de atendimento clínico — desde o agendamento até o registo final da consulta — proporcionando maior controlo operacional para a clínica e melhor experiência para o paciente.

Requisitos Funcionais:

1. Ator: Paciente
   Este ator precisa de uma interface simples, intuitiva e focada no autoatendimento.

RF-PAC-01: Efetuar o registo e a autenticação no sistema.
RF-PAC-02: Agendar consultas médicas e exames, podendo filtrar por especialidade, médico disponível e localização exata (por exemplo, procurar disponibilidade em clínicas na zona da Pedreira ou noutros bairros específicos).
RF-PAC-03: Consultar o histórico de consultas passadas e visualizar as datas das próximas marcações.
RF-PAC-04: Cancelar ou reagendar consultas, respeitando regras automáticas de antecedência mínima configuradas no sistema.
RF-PAC-05: Visualizar e descarregar (download) em formato PDF os resultados de exames e laudos médicos.
RF-PAC-06: Subscrever ou comprar planos de saúde e aceder a uma área financeira para gerir pagamentos, faturas e recibos.
RF-PAC-07: Avaliar a qualidade do atendimento e do médico após a realização da consulta.
RF-PAC-08: Receber notificações de confirmação e lembretes de consultas agendadas (por exemplo, 24 horas antes).
RF-PAC-09: Visualizar o estado atual das consultas (agendada, em espera, em atendimento, finalizada ou cancelada).

2. Ator: Médico
   O foco deste ator é a agilidade clínica. O ecrã não pode ter distrações, focando-se totalmente no paciente.

RF-MED-01: Autenticar-se de forma segura no sistema.
RF-MED-02: Visualizar o calendário e a listagem completa de consultas marcadas para o dia ou para a semana.
RF-MED-03: Gerir a própria agenda, podendo bloquear horários específicos, registar pausas ou períodos de férias.
RF-MED-04: Aceder ao Processo Clínico (Prontuário Eletrónico) do paciente para registar a anamnese, os sintomas, a evolução clínica e os diagnósticos da consulta atual.
RF-MED-05: Emitir e assinar digitalmente prescrições médicas (receitas), atestados e requisições para novos exames.
RF-MED-06: Consultar o histórico integral do paciente, incluindo resultados de exames antigos, diretamente no ecrã de atendimento antes e durante a consulta.
RF-MED-07: Visualizar em tempo real o estado das consultas (em espera, em atendimento, finalizada), com atualização automática da fila de pacientes.
RF-MED-08: Aceder apenas aos prontuários de pacientes autorizados, respeitando regras de controlo de acesso.

3. Ator: Administrador da Clínica
   Este ator é o motor do sistema. Ele faz a gestão da clínica, lida com imprevistos e gere o fluxo financeiro.

RF-REC-01: Registar a chegada do paciente (check-in) e alterar o estado da consulta para "A aguardar atendimento", atualizando de imediato a fila visível no ecrã do médico.
RF-REC-02: Registar no sistema novos médicos, associando-lhes as respetivas especialidades, horários de trabalho e gabinetes/consultórios.
RF-REC-03: Gerir as informações da própria clínica e os acordos ou tabelas de preços com as diferentes seguradoras e planos de saúde.
RF-REC-04: Fazer o carregamento (upload) de ficheiros com os resultados de exames provenientes de laboratórios externos diretamente para o perfil do paciente.
RF-REC-05: Gerar relatórios administrativos e financeiros, controlando o volume de consultas realizadas (particulares versus planos de saúde) para efeitos de faturação e cálculo de honorários.
RF-REC-06: Gerir o estado das consultas ao longo do fluxo completo (agendada, confirmada, em espera, em atendimento, finalizada, cancelada).
RF-REC-07: Configurar regras de agendamento da clínica, incluindo duração das consultas, intervalos entre atendimentos e antecedência mínima para cancelamento.
RF-REC-08: Registar pagamentos associados às consultas e controlar o estado financeiro (pago, pendente, cancelado).
Requisitos Transversais (Indispensáveis)
RF-TR-01: O sistema deve garantir o controlo de acesso baseado em perfis (paciente, médico e administrador), restringindo funcionalidades e dados conforme o tipo de utilizador.
RF-TR-02: O sistema deve manter um registo de auditoria (log) de todas as ações críticas, incluindo acessos e alterações ao prontuário do paciente.
RF-TR-03: O sistema deve garantir que não existam conflitos de horários no agendamento de consultas.
RF-TR-04: O sistema deve garantir a persistência e integridade dos dados clínicos registados.

Requisitos Não Funcionais:

1. Segurança
   RNF-SEC-01: O sistema deve garantir a confidencialidade dos dados sensíveis dos pacientes, implementando mecanismos de criptografia em trânsito (HTTPS) e, sempre que possível, em repouso.
   RNF-SEC-02: O sistema deve implementar controlo de acesso baseado em perfis (RBAC), garantindo que cada utilizador apenas acede às funcionalidades e dados autorizados.
   RNF-SEC-03: O sistema deve manter sessões seguras, com expiração automática após período de inatividade.
   RNF-SEC-04: O sistema deve registar logs de auditoria para ações críticas, incluindo acessos e alterações a dados clínicos.
   RNF-SEC-05: O sistema deve estar em conformidade com a legislação de proteção de dados aplicável (ex: LGPD).

2. Desempenho
   RNF-DES-01: O sistema deve responder a requisições de utilizadores em até 2 segundos em condições normais de uso.
   RNF-DES-02: O sistema deve suportar múltiplos utilizadores simultâneos sem degradação significativa de desempenho.
   RNF-DES-03: O sistema deve garantir tempo de carregamento reduzido para agendas e prontuários, mesmo com grande volume de dados.

3. Escalabilidade
   RNF-ESC-01: O sistema deve permitir escalabilidade horizontal, possibilitando a adição de novos servidores para suportar aumento de carga.
   RNF-ESC-02: O sistema deve ser projetado de forma stateless sempre que possível, permitindo distribuição de carga.
   RNF-ESC-03: O sistema deve suportar crescimento no volume de dados sem impacto significativo na performance.

4. Disponibilidade
   RNF-DISP-01: O sistema deve ter disponibilidade mínima de 99% durante horário operacional da clínica.
   RNF-DISP-02: O sistema deve ser resiliente a falhas, garantindo recuperação rápida em caso de erro.

5. Backup e Recuperação
   RNF-BKP-01: O sistema deve permitir a realização de backups periódicos dos dados.
   RNF-BKP-02: O sistema deve permitir a restauração de dados a partir de backups.
   RNF-BKP-03: O sistema deve garantir integridade dos dados após processos de recuperação.

6. Usabilidade
   RNF-USA-01: O sistema deve possuir interface intuitiva e de fácil utilização para todos os perfis de utilizadores.
   RNF-USA-02: O sistema deve minimizar o número de ações necessárias para tarefas comuns (ex: agendamento e registo de consulta).
   RNF-USA-03: O sistema deve apresentar feedback visual claro para ações realizadas (ex: sucesso, erro, carregamento).

7. Compatibilidade
   RNF-COMP-01: O sistema deve ser acessível através dos principais navegadores modernos (Chrome, Firefox, Edge).
   RNF-COMP-02: O sistema deve ser responsivo, permitindo utilização em diferentes dispositivos (desktop, tablet e mobile).

8. Manutenibilidade
   RNF-MAN-01: O sistema deve ser desenvolvido com código modular e organizado, facilitando manutenção e evolução.
   RNF-MAN-02: O sistema deve possuir documentação técnica adequada.
   RNF-MAN-03: O sistema deve permitir atualização sem perda de dados existentes.

9. Integração
   RNF-INT-01: O sistema deve disponibilizar APIs para integração com sistemas externos.
   RNF-INT-02: O sistema deve suportar formatos padrão de troca de dados (ex: JSON, REST).

10. Confiabilidade
    RNF-CONF-01: O sistema deve garantir consistência dos dados mesmo em caso de falhas.
    RNF-CONF-02: O sistema deve evitar perda de dados em operações críticas (ex: registo de consulta).

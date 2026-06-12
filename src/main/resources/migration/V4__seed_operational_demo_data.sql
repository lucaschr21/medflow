--
-- V4: Dados operacionais adicionais para demonstrar as telas de recepção e médico
--
-- Complementa o seed anterior com consultas do dia em horários úteis,
-- incluindo estados de fila e um registro de atendimento concluído.
--

-- Consultas adicionais do médico "medic" (usuario a72c..., medico f01...)
-- Horários posicionados antes do bloqueio do almoço e no fim da tarde.
INSERT INTO medflow.consulta (
    id,
    usuario_id,
    medico_id,
    consultorio_id,
    alocacao_medico_id,
    data_hora_inicio,
    data_hora_fim,
    status,
    tipo_consulta,
    motivo,
    created_at,
    last_modified_at,
    version,
    created_by,
    last_modified_by
) VALUES
('a2000000-0000-0000-0000-000000000001', 'e0000000-0000-0000-0000-000000000005', 'f0000000-0000-0000-0000-000000000001', 'c0000000-0000-0000-0000-000000000001', 'a1000000-0000-0000-0000-000000000001', CURRENT_DATE + TIME '10:30', CURRENT_DATE + TIME '11:00', 'AGENDADA', 'Cardiologia', 'Revisão de exames laboratoriais', now(), now(), 0, 'seed', 'seed'),
('a2000000-0000-0000-0000-000000000002', 'e0000000-0000-0000-0000-000000000009', 'f0000000-0000-0000-0000-000000000001', 'c0000000-0000-0000-0000-000000000001', 'a1000000-0000-0000-0000-000000000001', CURRENT_DATE + TIME '11:00', CURRENT_DATE + TIME '11:30', 'CONFIRMADA', 'Neurologia', 'Dor de cabeça recorrente', now(), now(), 0, 'seed', 'seed'),
('a2000000-0000-0000-0000-000000000003', 'e0000000-0000-0000-0000-000000000010', 'f0000000-0000-0000-0000-000000000001', 'c0000000-0000-0000-0000-000000000001', 'a1000000-0000-0000-0000-000000000001', CURRENT_DATE + TIME '16:00', CURRENT_DATE + TIME '16:30', 'EM_ESPERA', 'Cardiologia', 'Aguardando liberação da sala', now(), now(), 0, 'seed', 'seed'),
('a2000000-0000-0000-0000-000000000004', 'e0000000-0000-0000-0000-000000000005', 'f0000000-0000-0000-0000-000000000001', 'c0000000-0000-0000-0000-000000000001', 'a1000000-0000-0000-0000-000000000001', CURRENT_DATE + TIME '16:30', CURRENT_DATE + TIME '17:00', 'EM_ATENDIMENTO', 'Cardiologia', 'Consulta de retorno em andamento', now(), now(), 0, 'seed', 'seed');

-- Consultas adicionais do médico "medic2" (Ana Souza), para reforçar a fila de atendimento.
INSERT INTO medflow.consulta (
    id,
    usuario_id,
    medico_id,
    consultorio_id,
    alocacao_medico_id,
    data_hora_inicio,
    data_hora_fim,
    status,
    tipo_consulta,
    motivo,
    created_at,
    last_modified_at,
    version,
    created_by,
    last_modified_by
) VALUES
('a2000000-0000-0000-0000-000000000005', 'e0000000-0000-0000-0000-000000000005', 'f0000000-0000-0000-0000-000000000002', 'c0000000-0000-0000-0000-000000000004', 'a1000000-0000-0000-0000-000000000002', CURRENT_DATE + TIME '09:00', CURRENT_DATE + TIME '09:30', 'AGENDADA', 'Ginecologia', 'Pré-natal', now(), now(), 0, 'seed', 'seed'),
('a2000000-0000-0000-0000-000000000006', 'e0000000-0000-0000-0000-000000000009', 'f0000000-0000-0000-0000-000000000002', 'c0000000-0000-0000-0000-000000000004', 'a1000000-0000-0000-0000-000000000002', CURRENT_DATE + TIME '10:00', CURRENT_DATE + TIME '10:30', 'EM_ESPERA', 'Ginecologia', 'Retorno de exame preventivo', now(), now(), 0, 'seed', 'seed'),
('a2000000-0000-0000-0000-000000000007', 'e0000000-0000-0000-0000-000000000010', 'f0000000-0000-0000-0000-000000000002', 'c0000000-0000-0000-0000-000000000004', 'a1000000-0000-0000-0000-000000000002', CURRENT_DATE + TIME '10:30', CURRENT_DATE + TIME '11:00', 'EM_ATENDIMENTO', 'Pediatria', 'Paciente aguardando chamada', now(), now(), 0, 'seed', 'seed'),
('a2000000-0000-0000-0000-000000000008', 'e0000000-0000-0000-0000-000000000005', 'f0000000-0000-0000-0000-000000000002', 'c0000000-0000-0000-0000-000000000004', 'a1000000-0000-0000-0000-000000000002', CURRENT_DATE + TIME '11:00', CURRENT_DATE + TIME '11:30', 'FINALIZADA', 'Pediatria', 'Atendimento com vacinação', now(), now(), 0, 'seed', 'seed');

-- Registro de atendimento associado a uma consulta já concluída, para preencher telas
-- de histórico e demonstrar a trilha completa do atendimento.
INSERT INTO medflow.consulta (
    id,
    usuario_id,
    medico_id,
    consultorio_id,
    alocacao_medico_id,
    data_hora_inicio,
    data_hora_fim,
    status,
    tipo_consulta,
    motivo,
    created_at,
    last_modified_at,
    version,
    created_by,
    last_modified_by
) VALUES
('a2000000-0000-0000-0000-000000000009', 'e0000000-0000-0000-0000-000000000009', 'f0000000-0000-0000-0000-000000000002', 'c0000000-0000-0000-0000-000000000004', 'a1000000-0000-0000-0000-000000000002', (CURRENT_DATE - 1) + TIME '15:00', (CURRENT_DATE - 1) + TIME '15:30', 'FINALIZADA', 'Ginecologia', 'Revisão pós-consulta', now(), now(), 0, 'seed', 'seed');

INSERT INTO medflow.registro_atendimento (
    id,
    consulta_id,
    medico_id,
    queixa_principal,
    anamnese,
    conduta,
    observacoes,
    created_at,
    last_modified_at,
    version,
    created_by,
    last_modified_by
) VALUES
('a3000000-0000-0000-0000-000000000001', 'a2000000-0000-0000-0000-000000000009', 'f0000000-0000-0000-0000-000000000002', 'Retorno de prevenção', 'Paciente compareceu sem queixas agudas, referindo apenas necessidade de revisão de rotina e atualização de exames.', 'Orientado seguimento anual, manutenção de rotina de exames e retorno se houver novos sintomas.', 'Sem intercorrências durante o atendimento.', now(), now(), 0, 'seed', 'seed');

--
-- V3: Dados iniciais (seed) para desenvolvimento/demonstração
--
-- 1 org, 4 unidades, 10 consultórios, 12 especialidades,
-- 10 usuários, 6 médicos, alocações, agendas, bloqueios, 24 consultas.
--

INSERT INTO medflow.organizacao (id, nome, email, telefone, cor_primaria, created_at, last_modified_at, version, created_by, last_modified_by) VALUES
('a0000000-0000-0000-0000-000000000001', 'Clínica Medflow', 'contato@medflow.com', '1130000000', '#14b8a6', now(), now(), 0, 'seed', 'seed');

INSERT INTO medflow.unidade (id, organizacao_id, nome, telefone, cep, logradouro, numero, complemento, bairro, cidade, uf, created_at, last_modified_at, version, created_by, last_modified_by) VALUES
('b0000000-0000-0000-0000-000000000001', 'a0000000-0000-0000-0000-000000000001', 'Matriz Centro',    '1130001000', '01000000', 'Av. Paulista',     '1000', '10º andar',   'Bela Vista',  'São Paulo', 'SP', now(), now(), 0, 'seed', 'seed'),
('b0000000-0000-0000-0000-000000000002', 'a0000000-0000-0000-0000-000000000001', 'Filial Norte',     '1130002000', '02000000', 'Rua Augusta',      '500',  'Sala 1',      'Consolação',  'São Paulo', 'SP', now(), now(), 0, 'seed', 'seed'),
('b0000000-0000-0000-0000-000000000003', 'a0000000-0000-0000-0000-000000000001', 'Filial Sul',       '1130003000', '03000000', 'Av. Santo Amaro',  '700',  'Térreo',      'Santo Amaro', 'São Paulo', 'SP', now(), now(), 0, 'seed', 'seed'),
('b0000000-0000-0000-0000-000000000004', 'a0000000-0000-0000-0000-000000000001', 'Unidade Guarulhos','1130004000', '04000000', 'Rua Dom Pedro II', '200',  'Bloco A',     'Centro',      'Guarulhos', 'SP', now(), now(), 0, 'seed', 'seed');

INSERT INTO medflow.consultorio (id, unidade_id, nome, sala, created_at, last_modified_at, version, created_by, last_modified_by) VALUES
('c0000000-0000-0000-0000-000000000001', 'b0000000-0000-0000-0000-000000000001', 'Consultório 101', '101', now(), now(), 0, 'seed', 'seed'),
('c0000000-0000-0000-0000-000000000002', 'b0000000-0000-0000-0000-000000000001', 'Consultório 102', '102', now(), now(), 0, 'seed', 'seed'),
('c0000000-0000-0000-0000-000000000003', 'b0000000-0000-0000-0000-000000000001', 'Consultório 103', '103', now(), now(), 0, 'seed', 'seed'),
('c0000000-0000-0000-0000-000000000004', 'b0000000-0000-0000-0000-000000000002', 'Consultório 201', '201', now(), now(), 0, 'seed', 'seed'),
('c0000000-0000-0000-0000-000000000005', 'b0000000-0000-0000-0000-000000000002', 'Consultório 202', '202', now(), now(), 0, 'seed', 'seed'),
('c0000000-0000-0000-0000-000000000006', 'b0000000-0000-0000-0000-000000000003', 'Consultório 301', '301', now(), now(), 0, 'seed', 'seed'),
('c0000000-0000-0000-0000-000000000007', 'b0000000-0000-0000-0000-000000000003', 'Consultório 302', '302', now(), now(), 0, 'seed', 'seed'),
('c0000000-0000-0000-0000-000000000008', 'b0000000-0000-0000-0000-000000000004', 'Consultório 401', '401', now(), now(), 0, 'seed', 'seed'),
('c0000000-0000-0000-0000-000000000009', 'b0000000-0000-0000-0000-000000000004', 'Consultório 402', '402', now(), now(), 0, 'seed', 'seed'),
('c0000000-0000-0000-0000-000000000010', 'b0000000-0000-0000-0000-000000000004', 'Consultório 403', '403', now(), now(), 0, 'seed', 'seed');

INSERT INTO medflow.especialidade (id, nome, descricao, created_at, last_modified_at, version, created_by, last_modified_by) VALUES
('d0000000-0000-0000-0000-000000000001', 'Cardiologia',        'Doenças do coração e sistema circulatório', now(), now(), 0, 'seed', 'seed'),
('d0000000-0000-0000-0000-000000000002', 'Dermatologia',       'Cuidados com pele, cabelos e unhas', now(), now(), 0, 'seed', 'seed'),
('d0000000-0000-0000-0000-000000000003', 'Ginecologia',        'Saúde da mulher e sistema reprodutor', now(), now(), 0, 'seed', 'seed'),
('d0000000-0000-0000-0000-000000000004', 'Neurologia',         'Distúrbios do sistema nervoso', now(), now(), 0, 'seed', 'seed'),
('d0000000-0000-0000-0000-000000000005', 'Pediatria',          'Saúde de crianças e adolescentes', now(), now(), 0, 'seed', 'seed'),
('d0000000-0000-0000-0000-000000000006', 'Ortopedia',          'Ossos, articulações e músculos', now(), now(), 0, 'seed', 'seed'),
('d0000000-0000-0000-0000-000000000007', 'Oftalmologia',       'Doenças oculares e visão', now(), now(), 0, 'seed', 'seed'),
('d0000000-0000-0000-0000-000000000008', 'Psiquiatria',        'Saúde mental', now(), now(), 0, 'seed', 'seed'),
('d0000000-0000-0000-0000-000000000009', 'Endocrinologia',     'Distúrbios hormonais e metabólicos', now(), now(), 0, 'seed', 'seed'),
('d0000000-0000-0000-0000-000000000010', 'Gastroenterologia',  'Doenças do aparelho digestivo', now(), now(), 0, 'seed', 'seed'),
('d0000000-0000-0000-0000-000000000011', 'Urologia',           'Sistema urinário e reprodutor masculino', now(), now(), 0, 'seed', 'seed'),
('d0000000-0000-0000-0000-000000000012', 'Nutrologia',         'Nutrição clínica e dietoterapia', now(), now(), 0, 'seed', 'seed');

INSERT INTO medflow.usuario (id, organizacao_id, keycloak_id, created_at, last_modified_at, version, created_by, last_modified_by) VALUES
('e0000000-0000-0000-0000-000000000001', 'a0000000-0000-0000-0000-000000000001', 'd5ed50a4-519a-4f49-ace7-3d1206e88811', now(), now(), 0, 'seed', 'seed'),
('e0000000-0000-0000-0000-000000000002', 'a0000000-0000-0000-0000-000000000001', 'e8fb3e37-f185-48ef-9a28-7da3063f3a3c', now(), now(), 0, 'seed', 'seed'),
('e0000000-0000-0000-0000-000000000003', 'a0000000-0000-0000-0000-000000000001', 'a72c9062-82ba-4795-abf6-bf4c2c0e93c8', now(), now(), 0, 'seed', 'seed'),
('e0000000-0000-0000-0000-000000000004', 'a0000000-0000-0000-0000-000000000001', 'b72d51a3-93ca-5806-bc07-cf3d3d1e04d9', now(), now(), 0, 'seed', 'seed'),
('e0000000-0000-0000-0000-000000000005', 'a0000000-0000-0000-0000-000000000001', '408d14d1-cc8f-445b-bd2c-bd1721109d21', now(), now(), 0, 'seed', 'seed'),
('e0000000-0000-0000-0000-000000000006', 'a0000000-0000-0000-0000-000000000001', 'c83e62b4-a4db-6917-cd18-df4e4e2f15e0', now(), now(), 0, 'seed', 'seed'),
('e0000000-0000-0000-0000-000000000007', 'a0000000-0000-0000-0000-000000000001', 'd94f73c5-b5ec-7a28-de29-ef5f5f3a26f1', now(), now(), 0, 'seed', 'seed'),
('e0000000-0000-0000-0000-000000000008', 'a0000000-0000-0000-0000-000000000001', 'e05a84d6-c6fd-8b39-ef30-fa6a6a4b37a2', now(), now(), 0, 'seed', 'seed'),
('e0000000-0000-0000-0000-000000000009', 'a0000000-0000-0000-0000-000000000001', '519e25e2-dd01-556c-ce39-ce2832210e32', now(), now(), 0, 'seed', 'seed'),
('e0000000-0000-0000-0000-000000000010', 'a0000000-0000-0000-0000-000000000001', '62af36f3-ee12-667d-df40-df3943321f43', now(), now(), 0, 'seed', 'seed');

INSERT INTO medflow.medico (id, usuario_id, created_at, last_modified_at, version, created_by, last_modified_by) VALUES
('f0000000-0000-0000-0000-000000000001', 'e0000000-0000-0000-0000-000000000003', now(), now(), 0, 'seed', 'seed'),
('f0000000-0000-0000-0000-000000000002', 'e0000000-0000-0000-0000-000000000004', now(), now(), 0, 'seed', 'seed'),
('f0000000-0000-0000-0000-000000000003', 'e0000000-0000-0000-0000-000000000006', now(), now(), 0, 'seed', 'seed'),
('f0000000-0000-0000-0000-000000000004', 'e0000000-0000-0000-0000-000000000007', now(), now(), 0, 'seed', 'seed'),
('f0000000-0000-0000-0000-000000000005', 'e0000000-0000-0000-0000-000000000008', now(), now(), 0, 'seed', 'seed'),
('f0000000-0000-0000-0000-000000000006', 'e0000000-0000-0000-0000-000000000001', now(), now(), 0, 'seed', 'seed');

INSERT INTO medflow.medico_especialidade (medico_id, especialidade_id) VALUES
('f0000000-0000-0000-0000-000000000001', 'd0000000-0000-0000-0000-000000000001'),
('f0000000-0000-0000-0000-000000000001', 'd0000000-0000-0000-0000-000000000004'),
('f0000000-0000-0000-0000-000000000002', 'd0000000-0000-0000-0000-000000000003'),
('f0000000-0000-0000-0000-000000000002', 'd0000000-0000-0000-0000-000000000005'),
('f0000000-0000-0000-0000-000000000003', 'd0000000-0000-0000-0000-000000000006'),
('f0000000-0000-0000-0000-000000000003', 'd0000000-0000-0000-0000-000000000002'),
('f0000000-0000-0000-0000-000000000004', 'd0000000-0000-0000-0000-000000000007'),
('f0000000-0000-0000-0000-000000000004', 'd0000000-0000-0000-0000-000000000009'),
('f0000000-0000-0000-0000-000000000005', 'd0000000-0000-0000-0000-000000000008'),
('f0000000-0000-0000-0000-000000000005', 'd0000000-0000-0000-0000-000000000010'),
('f0000000-0000-0000-0000-000000000006', 'd0000000-0000-0000-0000-000000000011'),
('f0000000-0000-0000-0000-000000000006', 'd0000000-0000-0000-0000-000000000012');

INSERT INTO medflow.alocacao_medico (id, medico_id, consultorio_id, data_inicio, created_at, last_modified_at, version, created_by, last_modified_by) VALUES
('a1000000-0000-0000-0000-000000000001', 'f0000000-0000-0000-0000-000000000001', 'c0000000-0000-0000-0000-000000000001', '2026-01-01', now(), now(), 0, 'seed', 'seed'),
('a1000000-0000-0000-0000-000000000002', 'f0000000-0000-0000-0000-000000000002', 'c0000000-0000-0000-0000-000000000004', '2026-01-01', now(), now(), 0, 'seed', 'seed'),
('a1000000-0000-0000-0000-000000000003', 'f0000000-0000-0000-0000-000000000003', 'c0000000-0000-0000-0000-000000000006', '2026-01-01', now(), now(), 0, 'seed', 'seed'),
('a1000000-0000-0000-0000-000000000004', 'f0000000-0000-0000-0000-000000000004', 'c0000000-0000-0000-0000-000000000008', '2026-01-01', now(), now(), 0, 'seed', 'seed'),
('a1000000-0000-0000-0000-000000000005', 'f0000000-0000-0000-0000-000000000005', 'c0000000-0000-0000-0000-000000000009', '2026-01-01', now(), now(), 0, 'seed', 'seed'),
('a1000000-0000-0000-0000-000000000006', 'f0000000-0000-0000-0000-000000000006', 'c0000000-0000-0000-0000-000000000003', '2026-01-01', now(), now(), 0, 'seed', 'seed');

-- Carlos: Seg-Sex 08:00-17:00
INSERT INTO medflow.agenda_medica (id, alocacao_medico_id, dia_semana, hora_inicio, hora_fim, created_at, last_modified_at, version, created_by, last_modified_by) VALUES
(gen_random_uuid(), 'a1000000-0000-0000-0000-000000000001', 'MONDAY','08:00','17:00',now(),now(),0,'seed','seed'),
(gen_random_uuid(), 'a1000000-0000-0000-0000-000000000001', 'TUESDAY','08:00','17:00',now(),now(),0,'seed','seed'),
(gen_random_uuid(), 'a1000000-0000-0000-0000-000000000001', 'WEDNESDAY','08:00','17:00',now(),now(),0,'seed','seed'),
(gen_random_uuid(), 'a1000000-0000-0000-0000-000000000001', 'THURSDAY','08:00','17:00',now(),now(),0,'seed','seed'),
(gen_random_uuid(), 'a1000000-0000-0000-0000-000000000001', 'FRIDAY','08:00','12:00',now(),now(),0,'seed','seed');

-- Ana: Seg-Qui 09:00-18:00
INSERT INTO medflow.agenda_medica (id, alocacao_medico_id, dia_semana, hora_inicio, hora_fim, created_at, last_modified_at, version, created_by, last_modified_by) VALUES
(gen_random_uuid(), 'a1000000-0000-0000-0000-000000000002', 'MONDAY','09:00','18:00',now(),now(),0,'seed','seed'),
(gen_random_uuid(), 'a1000000-0000-0000-0000-000000000002', 'TUESDAY','09:00','18:00',now(),now(),0,'seed','seed'),
(gen_random_uuid(), 'a1000000-0000-0000-0000-000000000002', 'WEDNESDAY','09:00','18:00',now(),now(),0,'seed','seed'),
(gen_random_uuid(), 'a1000000-0000-0000-0000-000000000002', 'THURSDAY','09:00','18:00',now(),now(),0,'seed','seed');

-- Ricardo: Seg-Sáb 07:00-13:00
INSERT INTO medflow.agenda_medica (id, alocacao_medico_id, dia_semana, hora_inicio, hora_fim, created_at, last_modified_at, version, created_by, last_modified_by) VALUES
(gen_random_uuid(), 'a1000000-0000-0000-0000-000000000003', 'MONDAY','07:00','13:00',now(),now(),0,'seed','seed'),
(gen_random_uuid(), 'a1000000-0000-0000-0000-000000000003', 'TUESDAY','07:00','13:00',now(),now(),0,'seed','seed'),
(gen_random_uuid(), 'a1000000-0000-0000-0000-000000000003', 'WEDNESDAY','07:00','13:00',now(),now(),0,'seed','seed'),
(gen_random_uuid(), 'a1000000-0000-0000-0000-000000000003', 'THURSDAY','07:00','13:00',now(),now(),0,'seed','seed'),
(gen_random_uuid(), 'a1000000-0000-0000-0000-000000000003', 'FRIDAY','07:00','13:00',now(),now(),0,'seed','seed'),
(gen_random_uuid(), 'a1000000-0000-0000-0000-000000000003', 'SATURDAY','08:00','12:00',now(),now(),0,'seed','seed');

-- Juliana: Ter-Sex 10:00-19:00
INSERT INTO medflow.agenda_medica (id, alocacao_medico_id, dia_semana, hora_inicio, hora_fim, created_at, last_modified_at, version, created_by, last_modified_by) VALUES
(gen_random_uuid(), 'a1000000-0000-0000-0000-000000000004', 'TUESDAY','10:00','19:00',now(),now(),0,'seed','seed'),
(gen_random_uuid(), 'a1000000-0000-0000-0000-000000000004', 'WEDNESDAY','10:00','19:00',now(),now(),0,'seed','seed'),
(gen_random_uuid(), 'a1000000-0000-0000-0000-000000000004', 'THURSDAY','10:00','19:00',now(),now(),0,'seed','seed'),
(gen_random_uuid(), 'a1000000-0000-0000-0000-000000000004', 'FRIDAY','10:00','19:00',now(),now(),0,'seed','seed');

-- Paulo: Seg-Sex 13:00-20:00
INSERT INTO medflow.agenda_medica (id, alocacao_medico_id, dia_semana, hora_inicio, hora_fim, created_at, last_modified_at, version, created_by, last_modified_by) VALUES
(gen_random_uuid(), 'a1000000-0000-0000-0000-000000000005', 'MONDAY','13:00','20:00',now(),now(),0,'seed','seed'),
(gen_random_uuid(), 'a1000000-0000-0000-0000-000000000005', 'TUESDAY','13:00','20:00',now(),now(),0,'seed','seed'),
(gen_random_uuid(), 'a1000000-0000-0000-0000-000000000005', 'WEDNESDAY','13:00','20:00',now(),now(),0,'seed','seed'),
(gen_random_uuid(), 'a1000000-0000-0000-0000-000000000005', 'THURSDAY','13:00','20:00',now(),now(),0,'seed','seed'),
(gen_random_uuid(), 'a1000000-0000-0000-0000-000000000005', 'FRIDAY','13:00','20:00',now(),now(),0,'seed','seed');

-- root/admin: Seg/Qua/Sex 14:00-18:00
INSERT INTO medflow.agenda_medica (id, alocacao_medico_id, dia_semana, hora_inicio, hora_fim, created_at, last_modified_at, version, created_by, last_modified_by) VALUES
(gen_random_uuid(), 'a1000000-0000-0000-0000-000000000006', 'MONDAY','14:00','18:00',now(),now(),0,'seed','seed'),
(gen_random_uuid(), 'a1000000-0000-0000-0000-000000000006', 'WEDNESDAY','14:00','18:00',now(),now(),0,'seed','seed'),
(gen_random_uuid(), 'a1000000-0000-0000-0000-000000000006', 'FRIDAY','14:00','18:00',now(),now(),0,'seed','seed');

INSERT INTO medflow.bloqueio_agenda (id, medico_id, consultorio_id, inicio, fim, motivo, tipo, created_at, last_modified_at, version, created_by, last_modified_by) VALUES
(gen_random_uuid(), 'f0000000-0000-0000-0000-000000000001', 'c0000000-0000-0000-0000-000000000001', (CURRENT_DATE+2)+TIME '12:00', (CURRENT_DATE+2)+TIME '14:00', 'Reunião clínica semanal', 'PAUSA', now(),now(),0,'seed','seed'),
(gen_random_uuid(), 'f0000000-0000-0000-0000-000000000001', 'c0000000-0000-0000-0000-000000000001', (CURRENT_DATE+10)+TIME '08:00', (CURRENT_DATE+10)+TIME '17:00', 'Feriado municipal', 'FERIAS', now(),now(),0,'seed','seed'),
(gen_random_uuid(), 'f0000000-0000-0000-0000-000000000002', 'c0000000-0000-0000-0000-000000000004', (CURRENT_DATE+5)+TIME '08:00', (CURRENT_DATE+5)+TIME '18:00', 'Congresso de Pediatria', 'INDISPONIBILIDADE', now(),now(),0,'seed','seed'),
(gen_random_uuid(), 'f0000000-0000-0000-0000-000000000003', 'c0000000-0000-0000-0000-000000000006', (CURRENT_DATE+1)+TIME '07:00', (CURRENT_DATE+1)+TIME '08:00', 'Ajuste de horário', 'OUTRO', now(),now(),0,'seed','seed'),
(gen_random_uuid(), 'f0000000-0000-0000-0000-000000000004', 'c0000000-0000-0000-0000-000000000008', (CURRENT_DATE+3)+TIME '10:00', (CURRENT_DATE+3)+TIME '11:00', 'Pausa', 'PAUSA', now(),now(),0,'seed','seed'),
(gen_random_uuid(), 'f0000000-0000-0000-0000-000000000005', 'c0000000-0000-0000-0000-000000000009', (CURRENT_DATE+7)+TIME '13:00', (CURRENT_DATE+7)+TIME '20:00', 'Problema no consultório', 'INDISPONIBILIDADE', now(),now(),0,'seed','seed'),
(gen_random_uuid(), 'f0000000-0000-0000-0000-000000000005', 'c0000000-0000-0000-0000-000000000009', (CURRENT_DATE+14)+TIME '08:00', (CURRENT_DATE+21)+TIME '18:00', 'Férias', 'FERIAS', now(),now(),0,'seed','seed'),
(gen_random_uuid(), 'f0000000-0000-0000-0000-000000000006', 'c0000000-0000-0000-0000-000000000003', (CURRENT_DATE+4)+TIME '14:00', (CURRENT_DATE+4)+TIME '16:00', 'Manutenção', 'OUTRO', now(),now(),0,'seed','seed');

-- Consultas: Maria (e05) com Carlos (f01)
INSERT INTO medflow.consulta (id, usuario_id, medico_id, consultorio_id, alocacao_medico_id, data_hora_inicio, data_hora_fim, status, tipo_consulta, motivo, created_at, last_modified_at, version, created_by, last_modified_by) VALUES
(gen_random_uuid(),'e0000000-0000-0000-0000-000000000005','f0000000-0000-0000-0000-000000000001','c0000000-0000-0000-0000-000000000001','a1000000-0000-0000-0000-000000000001',(CURRENT_DATE+1)+TIME'09:00',(CURRENT_DATE+1)+TIME'09:30','AGENDADA','Cardiologia','Check-up de rotina',now(),now(),0,'seed','seed'),
(gen_random_uuid(),'e0000000-0000-0000-0000-000000000005','f0000000-0000-0000-0000-000000000001','c0000000-0000-0000-0000-000000000001','a1000000-0000-0000-0000-000000000001',CURRENT_DATE+TIME'10:00',CURRENT_DATE+TIME'10:30','CONFIRMADA','Neurologia','Dor de cabeça frequente',now(),now(),0,'seed','seed'),
(gen_random_uuid(),'e0000000-0000-0000-0000-000000000005','f0000000-0000-0000-0000-000000000001','c0000000-0000-0000-0000-000000000001','a1000000-0000-0000-0000-000000000001',CURRENT_DATE+TIME'14:00',CURRENT_DATE+TIME'14:30','EM_ESPERA','Cardiologia','Retorno de exames',now(),now(),0,'seed','seed'),
(gen_random_uuid(),'e0000000-0000-0000-0000-000000000005','f0000000-0000-0000-0000-000000000001','c0000000-0000-0000-0000-000000000001','a1000000-0000-0000-0000-000000000001',CURRENT_DATE+TIME'15:00',CURRENT_DATE+TIME'15:30','EM_ATENDIMENTO','Neurologia','Formigamento nos braços',now(),now(),0,'seed','seed'),
(gen_random_uuid(),'e0000000-0000-0000-0000-000000000005','f0000000-0000-0000-0000-000000000002','c0000000-0000-0000-0000-000000000004','a1000000-0000-0000-0000-000000000002',(CURRENT_DATE-1)+TIME'11:00',(CURRENT_DATE-1)+TIME'11:30','FINALIZADA','Ginecologia','Exame preventivo',now(),now(),0,'seed','seed'),
(gen_random_uuid(),'e0000000-0000-0000-0000-000000000005','f0000000-0000-0000-0000-000000000001','c0000000-0000-0000-0000-000000000001','a1000000-0000-0000-0000-000000000001',(CURRENT_DATE-5)+TIME'16:00',(CURRENT_DATE-5)+TIME'16:30','CANCELADA','Cardiologia','Paciente cancelou',now(),now(),0,'seed','seed'),
(gen_random_uuid(),'e0000000-0000-0000-0000-000000000005','f0000000-0000-0000-0000-000000000003','c0000000-0000-0000-0000-000000000006','a1000000-0000-0000-0000-000000000003',(CURRENT_DATE-8)+TIME'08:00',(CURRENT_DATE-8)+TIME'08:30','FINALIZADA','Ortopedia','Dor lombar',now(),now(),0,'seed','seed'),
(gen_random_uuid(),'e0000000-0000-0000-0000-000000000005','f0000000-0000-0000-0000-000000000004','c0000000-0000-0000-0000-000000000008','a1000000-0000-0000-0000-000000000004',(CURRENT_DATE+6)+TIME'11:00',(CURRENT_DATE+6)+TIME'11:30','AGENDADA','Oftalmologia','Renovação de óculos',now(),now(),0,'seed','seed'),
(gen_random_uuid(),'e0000000-0000-0000-0000-000000000005','f0000000-0000-0000-0000-000000000005','c0000000-0000-0000-0000-000000000009','a1000000-0000-0000-0000-000000000005',(CURRENT_DATE-4)+TIME'14:00',(CURRENT_DATE-4)+TIME'14:30','FINALIZADA','Gastroenterologia','Refluxo',now(),now(),0,'seed','seed');

-- João (e09)
INSERT INTO medflow.consulta (id, usuario_id, medico_id, consultorio_id, alocacao_medico_id, data_hora_inicio, data_hora_fim, status, tipo_consulta, motivo, created_at, last_modified_at, version, created_by, last_modified_by) VALUES
(gen_random_uuid(),'e0000000-0000-0000-0000-000000000009','f0000000-0000-0000-0000-000000000003','c0000000-0000-0000-0000-000000000006','a1000000-0000-0000-0000-000000000003',(CURRENT_DATE+2)+TIME'08:00',(CURRENT_DATE+2)+TIME'08:30','AGENDADA','Ortopedia','Dor no joelho',now(),now(),0,'seed','seed'),
(gen_random_uuid(),'e0000000-0000-0000-0000-000000000009','f0000000-0000-0000-0000-000000000003','c0000000-0000-0000-0000-000000000006','a1000000-0000-0000-0000-000000000003',(CURRENT_DATE-3)+TIME'09:00',(CURRENT_DATE-3)+TIME'09:30','FINALIZADA','Dermatologia','Manchas na pele',now(),now(),0,'seed','seed'),
(gen_random_uuid(),'e0000000-0000-0000-0000-000000000009','f0000000-0000-0000-0000-000000000004','c0000000-0000-0000-0000-000000000008','a1000000-0000-0000-0000-000000000004',(CURRENT_DATE+3)+TIME'11:00',(CURRENT_DATE+3)+TIME'11:30','AGENDADA','Oftalmologia','Dificuldade de visão',now(),now(),0,'seed','seed'),
(gen_random_uuid(),'e0000000-0000-0000-0000-000000000009','f0000000-0000-0000-0000-000000000005','c0000000-0000-0000-0000-000000000009','a1000000-0000-0000-0000-000000000005',(CURRENT_DATE-7)+TIME'15:00',(CURRENT_DATE-7)+TIME'15:30','FINALIZADA','Psiquiatria','Ansiedade',now(),now(),0,'seed','seed'),
(gen_random_uuid(),'e0000000-0000-0000-0000-000000000009','f0000000-0000-0000-0000-000000000005','c0000000-0000-0000-0000-000000000009','a1000000-0000-0000-0000-000000000005',(CURRENT_DATE+5)+TIME'16:00',(CURRENT_DATE+5)+TIME'16:30','AGENDADA','Psiquiatria','Retorno',now(),now(),0,'seed','seed'),
(gen_random_uuid(),'e0000000-0000-0000-0000-000000000009','f0000000-0000-0000-0000-000000000001','c0000000-0000-0000-0000-000000000001','a1000000-0000-0000-0000-000000000001',(CURRENT_DATE-6)+TIME'10:00',(CURRENT_DATE-6)+TIME'10:30','FINALIZADA','Cardiologia','Eletrocardiograma',now(),now(),0,'seed','seed');

-- Carla (e10)
INSERT INTO medflow.consulta (id, usuario_id, medico_id, consultorio_id, alocacao_medico_id, data_hora_inicio, data_hora_fim, status, tipo_consulta, motivo, created_at, last_modified_at, version, created_by, last_modified_by) VALUES
(gen_random_uuid(),'e0000000-0000-0000-0000-000000000010','f0000000-0000-0000-0000-000000000002','c0000000-0000-0000-0000-000000000004','a1000000-0000-0000-0000-000000000002',CURRENT_DATE+TIME'09:30',CURRENT_DATE+TIME'10:00','CONFIRMADA','Ginecologia','Pré-natal',now(),now(),0,'seed','seed'),
(gen_random_uuid(),'e0000000-0000-0000-0000-000000000010','f0000000-0000-0000-0000-000000000002','c0000000-0000-0000-0000-000000000004','a1000000-0000-0000-0000-000000000002',(CURRENT_DATE+1)+TIME'10:00',(CURRENT_DATE+1)+TIME'10:30','AGENDADA','Pediatria','Vacinação',now(),now(),0,'seed','seed'),
(gen_random_uuid(),'e0000000-0000-0000-0000-000000000010','f0000000-0000-0000-0000-000000000004','c0000000-0000-0000-0000-000000000008','a1000000-0000-0000-0000-000000000004',(CURRENT_DATE-2)+TIME'12:00',(CURRENT_DATE-2)+TIME'12:30','FINALIZADA','Endocrinologia','Tireoide',now(),now(),0,'seed','seed'),
(gen_random_uuid(),'e0000000-0000-0000-0000-000000000010','f0000000-0000-0000-0000-000000000003','c0000000-0000-0000-0000-000000000006','a1000000-0000-0000-0000-000000000003',(CURRENT_DATE-10)+TIME'07:30',(CURRENT_DATE-10)+TIME'08:00','NAO_COMPARECEU','Ortopedia','Torção tornozelo',now(),now(),0,'seed','seed'),
(gen_random_uuid(),'e0000000-0000-0000-0000-000000000010','f0000000-0000-0000-0000-000000000006','c0000000-0000-0000-0000-000000000003','a1000000-0000-0000-0000-000000000006',(CURRENT_DATE+4)+TIME'15:00',(CURRENT_DATE+4)+TIME'15:30','AGENDADA','Nutrologia','Plano alimentar',now(),now(),0,'seed','seed'),
(gen_random_uuid(),'e0000000-0000-0000-0000-000000000010','f0000000-0000-0000-0000-000000000001','c0000000-0000-0000-0000-000000000001','a1000000-0000-0000-0000-000000000001',CURRENT_DATE+TIME'08:00',CURRENT_DATE+TIME'08:30','EM_ESPERA','Cardiologia','Pressão alta',now(),now(),0,'seed','seed');

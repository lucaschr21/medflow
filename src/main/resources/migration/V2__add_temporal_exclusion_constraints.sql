CREATE EXTENSION IF NOT EXISTS btree_gist WITH SCHEMA medflow;

ALTER TABLE medflow.alocacao_medico
    ADD CONSTRAINT ex_alocacao_medico_sem_sobreposicao_por_medico
        EXCLUDE USING gist (
            medico_id WITH =,
            daterange(
                data_inicio,
                COALESCE(data_fim + 1, 'infinity'::date),
                '[)'
            ) WITH &&
        )
        WHERE (ativo);

ALTER TABLE medflow.alocacao_medico
    ADD CONSTRAINT ex_alocacao_medico_sem_sobreposicao_por_consultorio
        EXCLUDE USING gist (
            consultorio_id WITH =,
            daterange(
                data_inicio,
                COALESCE(data_fim + 1, 'infinity'::date),
                '[)'
            ) WITH &&
        )
        WHERE (ativo);

ALTER TABLE medflow.agenda_medica
    ADD CONSTRAINT ex_agenda_medica_sem_sobreposicao
        EXCLUDE USING gist (
            alocacao_medico_id WITH =,
            dia_semana WITH =,
            int4range(
                EXTRACT(EPOCH FROM hora_inicio)::integer,
                EXTRACT(EPOCH FROM hora_fim)::integer,
                '[)'
            ) WITH &&
        )
        WHERE (ativo);

ALTER TABLE medflow.bloqueio_agenda
    ADD CONSTRAINT ex_bloqueio_agenda_sem_sobreposicao_por_medico
        EXCLUDE USING gist (
            medico_id WITH =,
            tsrange(inicio, fim, '[)') WITH &&
        );

ALTER TABLE medflow.bloqueio_agenda
    ADD CONSTRAINT ex_bloqueio_agenda_sem_sobreposicao_por_consultorio
        EXCLUDE USING gist (
            consultorio_id WITH =,
            tsrange(inicio, fim, '[)') WITH &&
        );

ALTER TABLE medflow.consulta
    ADD CONSTRAINT ex_consulta_sem_sobreposicao_por_medico
        EXCLUDE USING gist (
            medico_id WITH =,
            tsrange(data_hora_inicio, data_hora_fim, '[)') WITH &&
        )
        WHERE (status IN ('AGENDADA', 'CONFIRMADA', 'EM_ESPERA', 'EM_ATENDIMENTO'));

ALTER TABLE medflow.consulta
    ADD CONSTRAINT ex_consulta_sem_sobreposicao_por_consultorio
        EXCLUDE USING gist (
            consultorio_id WITH =,
            tsrange(data_hora_inicio, data_hora_fim, '[)') WITH &&
        )
        WHERE (status IN ('AGENDADA', 'CONFIRMADA', 'EM_ESPERA', 'EM_ATENDIMENTO'));

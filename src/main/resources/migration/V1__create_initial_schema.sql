--
-- PostgreSQL database dump
--

-- Dumped from database version 18.3 (Debian 18.3-1.pgdg13+1)
-- Dumped by pg_dump version 18.3 (Debian 18.3-1.pgdg13+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: dayofweek; Type: TYPE; Schema: medflow; Owner: -
--

CREATE TYPE medflow.dayofweek AS ENUM (
    'FRIDAY',
    'MONDAY',
    'SATURDAY',
    'SUNDAY',
    'THURSDAY',
    'TUESDAY',
    'WEDNESDAY'
);


--
-- Name: statusconsulta; Type: TYPE; Schema: medflow; Owner: -
--

CREATE TYPE medflow.statusconsulta AS ENUM (
    'AGENDADA',
    'CANCELADA',
    'CONFIRMADA',
    'EM_ATENDIMENTO',
    'EM_ESPERA',
    'FINALIZADA',
    'NAO_COMPARECEU'
);


--
-- Name: tipobloqueioagenda; Type: TYPE; Schema: medflow; Owner: -
--

CREATE TYPE medflow.tipobloqueioagenda AS ENUM (
    'FERIAS',
    'INDISPONIBILIDADE',
    'OUTRO',
    'PAUSA'
);


--
-- Name: uf; Type: TYPE; Schema: medflow; Owner: -
--

CREATE TYPE medflow.uf AS ENUM (
    'AC',
    'AL',
    'AM',
    'AP',
    'BA',
    'CE',
    'DF',
    'ES',
    'GO',
    'MA',
    'MG',
    'MS',
    'MT',
    'PA',
    'PB',
    'PE',
    'PI',
    'PR',
    'RJ',
    'RN',
    'RO',
    'RR',
    'RS',
    'SC',
    'SE',
    'SP',
    'TO'
);


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: agenda_medica; Type: TABLE; Schema: medflow; Owner: -
--

CREATE TABLE medflow.agenda_medica (
    ativo boolean DEFAULT true NOT NULL,
    hora_fim time(0) without time zone NOT NULL,
    hora_inicio time(0) without time zone NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    last_modified_at timestamp(6) with time zone NOT NULL,
    version bigint NOT NULL,
    alocacao_medico_id uuid NOT NULL,
    id uuid NOT NULL,
    created_by character varying(255) NOT NULL,
    last_modified_by character varying(255) NOT NULL,
    dia_semana medflow.dayofweek NOT NULL,
    CONSTRAINT ck_agenda_medica_intervalo CHECK ((hora_fim > hora_inicio))
);


--
-- Name: agenda_medica_aud; Type: TABLE; Schema: medflow; Owner: -
--

CREATE TABLE medflow.agenda_medica_aud (
    ativo boolean,
    hora_fim time(0) without time zone,
    hora_inicio time(0) without time zone,
    rev integer NOT NULL,
    revtype smallint,
    alocacao_medico_id uuid,
    id uuid NOT NULL,
    dia_semana medflow.dayofweek
);


--
-- Name: alocacao_medico; Type: TABLE; Schema: medflow; Owner: -
--

CREATE TABLE medflow.alocacao_medico (
    ativo boolean DEFAULT true NOT NULL,
    data_fim date,
    data_inicio date NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    last_modified_at timestamp(6) with time zone NOT NULL,
    version bigint NOT NULL,
    consultorio_id uuid NOT NULL,
    id uuid NOT NULL,
    medico_id uuid NOT NULL,
    created_by character varying(255) NOT NULL,
    last_modified_by character varying(255) NOT NULL,
    CONSTRAINT ck_alocacao_medico_periodo CHECK (((data_fim IS NULL) OR (data_fim >= data_inicio)))
);


--
-- Name: alocacao_medico_aud; Type: TABLE; Schema: medflow; Owner: -
--

CREATE TABLE medflow.alocacao_medico_aud (
    ativo boolean,
    data_fim date,
    data_inicio date,
    rev integer NOT NULL,
    revtype smallint,
    consultorio_id uuid,
    id uuid NOT NULL,
    medico_id uuid
);


--
-- Name: anexo_consulta; Type: TABLE; Schema: medflow; Owner: -
--

CREATE TABLE medflow.anexo_consulta (
    created_at timestamp(6) with time zone NOT NULL,
    last_modified_at timestamp(6) with time zone NOT NULL,
    tamanho_bytes bigint NOT NULL,
    version bigint NOT NULL,
    consulta_id uuid NOT NULL,
    id uuid NOT NULL,
    content_type character varying(100) NOT NULL,
    descricao character varying(500),
    created_by character varying(255) NOT NULL,
    last_modified_by character varying(255) NOT NULL,
    nome_arquivo character varying(255) NOT NULL,
    arquivo oid NOT NULL,
    CONSTRAINT ck_anexo_consulta_dados CHECK (((tamanho_bytes >= 0) AND (char_length(TRIM(BOTH FROM nome_arquivo)) > 0) AND (char_length(TRIM(BOTH FROM content_type)) > 0)))
);


--
-- Name: anexo_consulta_aud; Type: TABLE; Schema: medflow; Owner: -
--

CREATE TABLE medflow.anexo_consulta_aud (
    rev integer NOT NULL,
    revtype smallint,
    tamanho_bytes bigint,
    consulta_id uuid,
    id uuid NOT NULL,
    content_type character varying(100),
    descricao character varying(500),
    nome_arquivo character varying(255),
    arquivo oid
);


--
-- Name: bloqueio_agenda; Type: TABLE; Schema: medflow; Owner: -
--

CREATE TABLE medflow.bloqueio_agenda (
    created_at timestamp(6) with time zone NOT NULL,
    fim timestamp(6) without time zone NOT NULL,
    inicio timestamp(6) without time zone NOT NULL,
    last_modified_at timestamp(6) with time zone NOT NULL,
    version bigint NOT NULL,
    consultorio_id uuid NOT NULL,
    id uuid NOT NULL,
    medico_id uuid NOT NULL,
    motivo character varying(500) NOT NULL,
    created_by character varying(255) NOT NULL,
    last_modified_by character varying(255) NOT NULL,
    tipo medflow.tipobloqueioagenda NOT NULL,
    CONSTRAINT ck_bloqueio_agenda_intervalo CHECK (((fim > inicio) AND (char_length(TRIM(BOTH FROM motivo)) > 0)))
);


--
-- Name: bloqueio_agenda_aud; Type: TABLE; Schema: medflow; Owner: -
--

CREATE TABLE medflow.bloqueio_agenda_aud (
    rev integer NOT NULL,
    revtype smallint,
    fim timestamp(6) without time zone,
    inicio timestamp(6) without time zone,
    consultorio_id uuid,
    id uuid NOT NULL,
    medico_id uuid,
    motivo character varying(500),
    tipo medflow.tipobloqueioagenda
);


--
-- Name: consulta; Type: TABLE; Schema: medflow; Owner: -
--

CREATE TABLE medflow.consulta (
    created_at timestamp(6) with time zone NOT NULL,
    data_hora_fim timestamp(6) without time zone NOT NULL,
    data_hora_inicio timestamp(6) without time zone NOT NULL,
    last_modified_at timestamp(6) with time zone NOT NULL,
    version bigint NOT NULL,
    alocacao_medico_id uuid NOT NULL,
    consultorio_id uuid NOT NULL,
    id uuid NOT NULL,
    medico_id uuid NOT NULL,
    usuario_id uuid NOT NULL,
    tipo_consulta character varying(80) NOT NULL,
    motivo character varying(500) NOT NULL,
    created_by character varying(255) NOT NULL,
    last_modified_by character varying(255) NOT NULL,
    status medflow.statusconsulta NOT NULL,
    CONSTRAINT ck_consulta_intervalo_campos_textuais CHECK (((data_hora_fim > data_hora_inicio) AND (char_length(TRIM(BOTH FROM tipo_consulta)) > 0) AND (char_length(TRIM(BOTH FROM motivo)) > 0)))
);


--
-- Name: consulta_aud; Type: TABLE; Schema: medflow; Owner: -
--

CREATE TABLE medflow.consulta_aud (
    rev integer NOT NULL,
    revtype smallint,
    data_hora_fim timestamp(6) without time zone,
    data_hora_inicio timestamp(6) without time zone,
    alocacao_medico_id uuid,
    consultorio_id uuid,
    id uuid NOT NULL,
    medico_id uuid,
    usuario_id uuid,
    tipo_consulta character varying(80),
    motivo character varying(500),
    status medflow.statusconsulta
);


--
-- Name: consultorio; Type: TABLE; Schema: medflow; Owner: -
--

CREATE TABLE medflow.consultorio (
    ativo boolean DEFAULT true NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    last_modified_at timestamp(6) with time zone NOT NULL,
    version bigint NOT NULL,
    id uuid NOT NULL,
    unidade_id uuid NOT NULL,
    sala character varying(40) NOT NULL,
    nome character varying(120) NOT NULL,
    created_by character varying(255) NOT NULL,
    last_modified_by character varying(255) NOT NULL,
    CONSTRAINT ck_consultorio_campos_textuais CHECK (((char_length(TRIM(BOTH FROM nome)) > 0) AND (char_length(TRIM(BOTH FROM sala)) > 0)))
);


--
-- Name: consultorio_aud; Type: TABLE; Schema: medflow; Owner: -
--

CREATE TABLE medflow.consultorio_aud (
    ativo boolean,
    rev integer NOT NULL,
    revtype smallint,
    id uuid NOT NULL,
    unidade_id uuid,
    sala character varying(40),
    nome character varying(255)
);


--
-- Name: especialidade; Type: TABLE; Schema: medflow; Owner: -
--

CREATE TABLE medflow.especialidade (
    ativo boolean DEFAULT true NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    last_modified_at timestamp(6) with time zone NOT NULL,
    version bigint NOT NULL,
    id uuid NOT NULL,
    nome character varying(120) NOT NULL,
    descricao character varying(500),
    created_by character varying(255) NOT NULL,
    last_modified_by character varying(255) NOT NULL,
    CONSTRAINT ck_especialidade_nome CHECK ((char_length(TRIM(BOTH FROM nome)) > 0))
);


--
-- Name: especialidade_aud; Type: TABLE; Schema: medflow; Owner: -
--

CREATE TABLE medflow.especialidade_aud (
    ativo boolean,
    rev integer NOT NULL,
    revtype smallint,
    id uuid NOT NULL,
    nome character varying(120),
    descricao character varying(500)
);


--
-- Name: medico; Type: TABLE; Schema: medflow; Owner: -
--

CREATE TABLE medflow.medico (
    ativo boolean DEFAULT true NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    last_modified_at timestamp(6) with time zone NOT NULL,
    version bigint NOT NULL,
    id uuid NOT NULL,
    usuario_id uuid NOT NULL,
    created_by character varying(255) NOT NULL,
    last_modified_by character varying(255) NOT NULL
);


--
-- Name: medico_aud; Type: TABLE; Schema: medflow; Owner: -
--

CREATE TABLE medflow.medico_aud (
    ativo boolean,
    rev integer NOT NULL,
    revtype smallint,
    id uuid NOT NULL,
    usuario_id uuid
);


--
-- Name: medico_especialidade; Type: TABLE; Schema: medflow; Owner: -
--

CREATE TABLE medflow.medico_especialidade (
    especialidade_id uuid NOT NULL,
    medico_id uuid NOT NULL
);


--
-- Name: medico_especialidade_aud; Type: TABLE; Schema: medflow; Owner: -
--

CREATE TABLE medflow.medico_especialidade_aud (
    rev integer NOT NULL,
    revtype smallint,
    especialidade_id uuid NOT NULL,
    medico_id uuid NOT NULL
);


--
-- Name: organizacao; Type: TABLE; Schema: medflow; Owner: -
--

CREATE TABLE medflow.organizacao (
    ativo boolean DEFAULT true NOT NULL,
    cor_primaria character varying(7) NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    last_modified_at timestamp(6) with time zone NOT NULL,
    version bigint NOT NULL,
    telefone character varying(11) NOT NULL,
    id uuid NOT NULL,
    logotipo_content_type character varying(100),
    nome character varying(120) NOT NULL,
    email character varying(254) NOT NULL,
    created_by character varying(255) NOT NULL,
    last_modified_by character varying(255) NOT NULL,
    logotipo oid,
    CONSTRAINT ck_organizacao_campos_textuais CHECK (((char_length(TRIM(BOTH FROM nome)) > 0) AND (char_length(TRIM(BOTH FROM email)) > 0) AND (char_length(TRIM(BOTH FROM telefone)) > 0) AND (char_length(TRIM(BOTH FROM cor_primaria)) > 0)))
);


--
-- Name: organizacao_aud; Type: TABLE; Schema: medflow; Owner: -
--

CREATE TABLE medflow.organizacao_aud (
    ativo boolean,
    rev integer NOT NULL,
    revtype smallint,
    cor_primaria character varying(7),
    telefone character varying(11),
    id uuid NOT NULL,
    email character varying(255),
    logotipo_content_type character varying(255),
    nome character varying(255),
    logotipo oid
);


--
-- Name: registro_atendimento; Type: TABLE; Schema: medflow; Owner: -
--

CREATE TABLE medflow.registro_atendimento (
    created_at timestamp(6) with time zone NOT NULL,
    last_modified_at timestamp(6) with time zone NOT NULL,
    version bigint NOT NULL,
    consulta_id uuid NOT NULL,
    id uuid NOT NULL,
    medico_id uuid NOT NULL,
    queixa_principal character varying(500) NOT NULL,
    observacoes character varying(10000),
    anamnese character varying(32600) NOT NULL,
    conduta character varying(32600) NOT NULL,
    created_by character varying(255) NOT NULL,
    last_modified_by character varying(255) NOT NULL,
    CONSTRAINT ck_registro_atendimento_campos_textuais CHECK (((char_length(TRIM(BOTH FROM queixa_principal)) > 0) AND (char_length(TRIM(BOTH FROM anamnese)) > 0) AND (char_length(TRIM(BOTH FROM conduta)) > 0)))
);


--
-- Name: registro_atendimento_aud; Type: TABLE; Schema: medflow; Owner: -
--

CREATE TABLE medflow.registro_atendimento_aud (
    rev integer NOT NULL,
    revtype smallint,
    consulta_id uuid,
    id uuid NOT NULL,
    medico_id uuid,
    queixa_principal character varying(500),
    anamnese character varying(32600),
    conduta character varying(32600),
    observacoes character varying(32600)
);


--
-- Name: revinfo; Type: TABLE; Schema: medflow; Owner: -
--

CREATE TABLE medflow.revinfo (
    rev integer NOT NULL,
    revtstmp bigint
);


--
-- Name: revinfo_seq; Type: SEQUENCE; Schema: medflow; Owner: -
--

CREATE SEQUENCE medflow.revinfo_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: unidade; Type: TABLE; Schema: medflow; Owner: -
--

CREATE TABLE medflow.unidade (
    ativo boolean DEFAULT true NOT NULL,
    cep character varying(8) NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    last_modified_at timestamp(6) with time zone NOT NULL,
    version bigint NOT NULL,
    telefone character varying(11) NOT NULL,
    id uuid NOT NULL,
    organizacao_id uuid NOT NULL,
    numero character varying(20) NOT NULL,
    bairro character varying(80) NOT NULL,
    cidade character varying(120) NOT NULL,
    complemento character varying(120),
    nome character varying(120) NOT NULL,
    logradouro character varying(160) NOT NULL,
    created_by character varying(255) NOT NULL,
    last_modified_by character varying(255) NOT NULL,
    uf medflow.uf NOT NULL,
    CONSTRAINT ck_unidade_campos_textuais CHECK (((char_length(TRIM(BOTH FROM nome)) > 0) AND (char_length(TRIM(BOTH FROM telefone)) > 0)))
);


--
-- Name: unidade_aud; Type: TABLE; Schema: medflow; Owner: -
--

CREATE TABLE medflow.unidade_aud (
    ativo boolean,
    rev integer NOT NULL,
    revtype smallint,
    cep character varying(8),
    telefone character varying(11),
    id uuid NOT NULL,
    organizacao_id uuid,
    numero character varying(20),
    bairro character varying(80),
    cidade character varying(120),
    complemento character varying(120),
    logradouro character varying(160),
    nome character varying(255),
    uf medflow.uf
);


--
-- Name: usuario; Type: TABLE; Schema: medflow; Owner: -
--

CREATE TABLE medflow.usuario (
    ativo boolean DEFAULT true NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    last_modified_at timestamp(6) with time zone NOT NULL,
    version bigint NOT NULL,
    id uuid NOT NULL,
    keycloak_id uuid NOT NULL,
    organizacao_id uuid NOT NULL,
    created_by character varying(255) NOT NULL,
    last_modified_by character varying(255) NOT NULL
);


--
-- Name: usuario_aud; Type: TABLE; Schema: medflow; Owner: -
--

CREATE TABLE medflow.usuario_aud (
    ativo boolean,
    rev integer NOT NULL,
    revtype smallint,
    id uuid NOT NULL,
    keycloak_id uuid,
    organizacao_id uuid
);


--
-- Name: agenda_medica_aud agenda_medica_aud_pkey; Type: CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.agenda_medica_aud
    ADD CONSTRAINT agenda_medica_aud_pkey PRIMARY KEY (rev, id);


--
-- Name: agenda_medica agenda_medica_pkey; Type: CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.agenda_medica
    ADD CONSTRAINT agenda_medica_pkey PRIMARY KEY (id);


--
-- Name: alocacao_medico_aud alocacao_medico_aud_pkey; Type: CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.alocacao_medico_aud
    ADD CONSTRAINT alocacao_medico_aud_pkey PRIMARY KEY (rev, id);


--
-- Name: alocacao_medico alocacao_medico_pkey; Type: CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.alocacao_medico
    ADD CONSTRAINT alocacao_medico_pkey PRIMARY KEY (id);


--
-- Name: anexo_consulta_aud anexo_consulta_aud_pkey; Type: CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.anexo_consulta_aud
    ADD CONSTRAINT anexo_consulta_aud_pkey PRIMARY KEY (rev, id);


--
-- Name: anexo_consulta anexo_consulta_pkey; Type: CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.anexo_consulta
    ADD CONSTRAINT anexo_consulta_pkey PRIMARY KEY (id);


--
-- Name: bloqueio_agenda_aud bloqueio_agenda_aud_pkey; Type: CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.bloqueio_agenda_aud
    ADD CONSTRAINT bloqueio_agenda_aud_pkey PRIMARY KEY (rev, id);


--
-- Name: bloqueio_agenda bloqueio_agenda_pkey; Type: CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.bloqueio_agenda
    ADD CONSTRAINT bloqueio_agenda_pkey PRIMARY KEY (id);


--
-- Name: consulta_aud consulta_aud_pkey; Type: CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.consulta_aud
    ADD CONSTRAINT consulta_aud_pkey PRIMARY KEY (rev, id);


--
-- Name: consulta consulta_pkey; Type: CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.consulta
    ADD CONSTRAINT consulta_pkey PRIMARY KEY (id);


--
-- Name: consultorio_aud consultorio_aud_pkey; Type: CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.consultorio_aud
    ADD CONSTRAINT consultorio_aud_pkey PRIMARY KEY (rev, id);


--
-- Name: consultorio consultorio_pkey; Type: CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.consultorio
    ADD CONSTRAINT consultorio_pkey PRIMARY KEY (id);


--
-- Name: especialidade_aud especialidade_aud_pkey; Type: CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.especialidade_aud
    ADD CONSTRAINT especialidade_aud_pkey PRIMARY KEY (rev, id);


--
-- Name: especialidade especialidade_nome_key; Type: CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.especialidade
    ADD CONSTRAINT especialidade_nome_key UNIQUE (nome);


--
-- Name: especialidade especialidade_pkey; Type: CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.especialidade
    ADD CONSTRAINT especialidade_pkey PRIMARY KEY (id);


--
-- Name: medico_aud medico_aud_pkey; Type: CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.medico_aud
    ADD CONSTRAINT medico_aud_pkey PRIMARY KEY (rev, id);


--
-- Name: medico_especialidade_aud medico_especialidade_aud_pkey; Type: CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.medico_especialidade_aud
    ADD CONSTRAINT medico_especialidade_aud_pkey PRIMARY KEY (rev, especialidade_id, medico_id);


--
-- Name: medico medico_pkey; Type: CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.medico
    ADD CONSTRAINT medico_pkey PRIMARY KEY (id);


--
-- Name: medico medico_usuario_id_key; Type: CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.medico
    ADD CONSTRAINT medico_usuario_id_key UNIQUE (usuario_id);


--
-- Name: organizacao_aud organizacao_aud_pkey; Type: CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.organizacao_aud
    ADD CONSTRAINT organizacao_aud_pkey PRIMARY KEY (rev, id);


--
-- Name: organizacao organizacao_pkey; Type: CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.organizacao
    ADD CONSTRAINT organizacao_pkey PRIMARY KEY (id);


--
-- Name: registro_atendimento_aud registro_atendimento_aud_pkey; Type: CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.registro_atendimento_aud
    ADD CONSTRAINT registro_atendimento_aud_pkey PRIMARY KEY (rev, id);


--
-- Name: registro_atendimento registro_atendimento_consulta_id_key; Type: CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.registro_atendimento
    ADD CONSTRAINT registro_atendimento_consulta_id_key UNIQUE (consulta_id);


--
-- Name: registro_atendimento registro_atendimento_pkey; Type: CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.registro_atendimento
    ADD CONSTRAINT registro_atendimento_pkey PRIMARY KEY (id);


--
-- Name: revinfo revinfo_pkey; Type: CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.revinfo
    ADD CONSTRAINT revinfo_pkey PRIMARY KEY (rev);


--
-- Name: agenda_medica uk_agenda_medica_alocacao_dia_horas; Type: CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.agenda_medica
    ADD CONSTRAINT uk_agenda_medica_alocacao_dia_horas UNIQUE (alocacao_medico_id, dia_semana, hora_inicio, hora_fim);


--
-- Name: consultorio uk_consultorio_unidade_sala; Type: CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.consultorio
    ADD CONSTRAINT uk_consultorio_unidade_sala UNIQUE (unidade_id, sala);


--
-- Name: medico_especialidade uk_medico_especialidade; Type: CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.medico_especialidade
    ADD CONSTRAINT uk_medico_especialidade PRIMARY KEY (medico_id, especialidade_id);


--
-- Name: unidade uk_unidade_organizacao_nome; Type: CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.unidade
    ADD CONSTRAINT uk_unidade_organizacao_nome UNIQUE (organizacao_id, nome);


--
-- Name: usuario uk_usuario_organizacao_keycloak; Type: CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.usuario
    ADD CONSTRAINT uk_usuario_organizacao_keycloak UNIQUE (organizacao_id, keycloak_id);


--
-- Name: unidade_aud unidade_aud_pkey; Type: CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.unidade_aud
    ADD CONSTRAINT unidade_aud_pkey PRIMARY KEY (rev, id);


--
-- Name: unidade unidade_pkey; Type: CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.unidade
    ADD CONSTRAINT unidade_pkey PRIMARY KEY (id);


--
-- Name: usuario_aud usuario_aud_pkey; Type: CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.usuario_aud
    ADD CONSTRAINT usuario_aud_pkey PRIMARY KEY (rev, id);


--
-- Name: usuario usuario_pkey; Type: CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id);


--
-- Name: ix_agenda_medica_alocacao_dia_semana; Type: INDEX; Schema: medflow; Owner: -
--

CREATE INDEX ix_agenda_medica_alocacao_dia_semana ON medflow.agenda_medica USING btree (alocacao_medico_id, dia_semana);


--
-- Name: ix_agenda_medica_alocacao_medico_id; Type: INDEX; Schema: medflow; Owner: -
--

CREATE INDEX ix_agenda_medica_alocacao_medico_id ON medflow.agenda_medica USING btree (alocacao_medico_id);


--
-- Name: ix_alocacao_medico_consultorio_data_inicio; Type: INDEX; Schema: medflow; Owner: -
--

CREATE INDEX ix_alocacao_medico_consultorio_data_inicio ON medflow.alocacao_medico USING btree (consultorio_id, data_inicio);


--
-- Name: ix_alocacao_medico_consultorio_id; Type: INDEX; Schema: medflow; Owner: -
--

CREATE INDEX ix_alocacao_medico_consultorio_id ON medflow.alocacao_medico USING btree (consultorio_id);


--
-- Name: ix_alocacao_medico_medico_data_inicio; Type: INDEX; Schema: medflow; Owner: -
--

CREATE INDEX ix_alocacao_medico_medico_data_inicio ON medflow.alocacao_medico USING btree (medico_id, data_inicio);


--
-- Name: ix_alocacao_medico_medico_id; Type: INDEX; Schema: medflow; Owner: -
--

CREATE INDEX ix_alocacao_medico_medico_id ON medflow.alocacao_medico USING btree (medico_id);


--
-- Name: ix_anexo_consulta_consulta_id; Type: INDEX; Schema: medflow; Owner: -
--

CREATE INDEX ix_anexo_consulta_consulta_id ON medflow.anexo_consulta USING btree (consulta_id);


--
-- Name: ix_bloqueio_agenda_consultorio_id; Type: INDEX; Schema: medflow; Owner: -
--

CREATE INDEX ix_bloqueio_agenda_consultorio_id ON medflow.bloqueio_agenda USING btree (consultorio_id);


--
-- Name: ix_bloqueio_agenda_consultorio_inicio; Type: INDEX; Schema: medflow; Owner: -
--

CREATE INDEX ix_bloqueio_agenda_consultorio_inicio ON medflow.bloqueio_agenda USING btree (consultorio_id, inicio);


--
-- Name: ix_bloqueio_agenda_medico_id; Type: INDEX; Schema: medflow; Owner: -
--

CREATE INDEX ix_bloqueio_agenda_medico_id ON medflow.bloqueio_agenda USING btree (medico_id);


--
-- Name: ix_bloqueio_agenda_medico_inicio; Type: INDEX; Schema: medflow; Owner: -
--

CREATE INDEX ix_bloqueio_agenda_medico_inicio ON medflow.bloqueio_agenda USING btree (medico_id, inicio);


--
-- Name: ix_consulta_alocacao_medico_id; Type: INDEX; Schema: medflow; Owner: -
--

CREATE INDEX ix_consulta_alocacao_medico_id ON medflow.consulta USING btree (alocacao_medico_id);


--
-- Name: ix_consulta_consultorio_data_hora_inicio; Type: INDEX; Schema: medflow; Owner: -
--

CREATE INDEX ix_consulta_consultorio_data_hora_inicio ON medflow.consulta USING btree (consultorio_id, data_hora_inicio);


--
-- Name: ix_consulta_consultorio_id; Type: INDEX; Schema: medflow; Owner: -
--

CREATE INDEX ix_consulta_consultorio_id ON medflow.consulta USING btree (consultorio_id);


--
-- Name: ix_consulta_medico_data_hora_inicio; Type: INDEX; Schema: medflow; Owner: -
--

CREATE INDEX ix_consulta_medico_data_hora_inicio ON medflow.consulta USING btree (medico_id, data_hora_inicio);


--
-- Name: ix_consulta_medico_id; Type: INDEX; Schema: medflow; Owner: -
--

CREATE INDEX ix_consulta_medico_id ON medflow.consulta USING btree (medico_id);


--
-- Name: ix_consulta_usuario_data_hora_inicio; Type: INDEX; Schema: medflow; Owner: -
--

CREATE INDEX ix_consulta_usuario_data_hora_inicio ON medflow.consulta USING btree (usuario_id, data_hora_inicio);


--
-- Name: ix_consulta_usuario_id; Type: INDEX; Schema: medflow; Owner: -
--

CREATE INDEX ix_consulta_usuario_id ON medflow.consulta USING btree (usuario_id);


--
-- Name: ix_consultorio_unidade_id; Type: INDEX; Schema: medflow; Owner: -
--

CREATE INDEX ix_consultorio_unidade_id ON medflow.consultorio USING btree (unidade_id);


--
-- Name: ix_medico_especialidade_especialidade_id; Type: INDEX; Schema: medflow; Owner: -
--

CREATE INDEX ix_medico_especialidade_especialidade_id ON medflow.medico_especialidade USING btree (especialidade_id);


--
-- Name: ix_medico_especialidade_medico_id; Type: INDEX; Schema: medflow; Owner: -
--

CREATE INDEX ix_medico_especialidade_medico_id ON medflow.medico_especialidade USING btree (medico_id);


--
-- Name: ix_medico_usuario_id; Type: INDEX; Schema: medflow; Owner: -
--

CREATE INDEX ix_medico_usuario_id ON medflow.medico USING btree (usuario_id);


--
-- Name: ix_organizacao_ativo; Type: INDEX; Schema: medflow; Owner: -
--

CREATE INDEX ix_organizacao_ativo ON medflow.organizacao USING btree (ativo);


--
-- Name: ix_registro_atendimento_consulta_id; Type: INDEX; Schema: medflow; Owner: -
--

CREATE INDEX ix_registro_atendimento_consulta_id ON medflow.registro_atendimento USING btree (consulta_id);


--
-- Name: ix_registro_atendimento_medico_id; Type: INDEX; Schema: medflow; Owner: -
--

CREATE INDEX ix_registro_atendimento_medico_id ON medflow.registro_atendimento USING btree (medico_id);


--
-- Name: ix_unidade_organizacao_ativo; Type: INDEX; Schema: medflow; Owner: -
--

CREATE INDEX ix_unidade_organizacao_ativo ON medflow.unidade USING btree (organizacao_id, ativo);


--
-- Name: ix_unidade_organizacao_id; Type: INDEX; Schema: medflow; Owner: -
--

CREATE INDEX ix_unidade_organizacao_id ON medflow.unidade USING btree (organizacao_id);


--
-- Name: ix_usuario_keycloak_id; Type: INDEX; Schema: medflow; Owner: -
--

CREATE INDEX ix_usuario_keycloak_id ON medflow.usuario USING btree (keycloak_id);


--
-- Name: ix_usuario_organizacao_id; Type: INDEX; Schema: medflow; Owner: -
--

CREATE INDEX ix_usuario_organizacao_id ON medflow.usuario USING btree (organizacao_id);


--
-- Name: bloqueio_agenda fk2hbsh1htdoo8xwx0mpiev4e2; Type: FK CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.bloqueio_agenda
    ADD CONSTRAINT fk2hbsh1htdoo8xwx0mpiev4e2 FOREIGN KEY (consultorio_id) REFERENCES medflow.consultorio(id);


--
-- Name: alocacao_medico_aud fk2rs1yn2yysakwu317k4togcy; Type: FK CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.alocacao_medico_aud
    ADD CONSTRAINT fk2rs1yn2yysakwu317k4togcy FOREIGN KEY (rev) REFERENCES medflow.revinfo(rev);


--
-- Name: bloqueio_agenda_aud fk3aq0uae7yp5j3xdli1pl5t75t; Type: FK CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.bloqueio_agenda_aud
    ADD CONSTRAINT fk3aq0uae7yp5j3xdli1pl5t75t FOREIGN KEY (rev) REFERENCES medflow.revinfo(rev);


--
-- Name: registro_atendimento_aud fk3pd7ogfrrll754b5ae3rp5e6b; Type: FK CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.registro_atendimento_aud
    ADD CONSTRAINT fk3pd7ogfrrll754b5ae3rp5e6b FOREIGN KEY (rev) REFERENCES medflow.revinfo(rev);


--
-- Name: consultorio fk4flyr8hpawcwg3p09aylajhsq; Type: FK CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.consultorio
    ADD CONSTRAINT fk4flyr8hpawcwg3p09aylajhsq FOREIGN KEY (unidade_id) REFERENCES medflow.unidade(id);


--
-- Name: medico fk4k58l20sg4pidog1kpa9g81rr; Type: FK CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.medico
    ADD CONSTRAINT fk4k58l20sg4pidog1kpa9g81rr FOREIGN KEY (usuario_id) REFERENCES medflow.usuario(id);


--
-- Name: organizacao_aud fk6he4qotwwkinmxdpm91infjgb; Type: FK CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.organizacao_aud
    ADD CONSTRAINT fk6he4qotwwkinmxdpm91infjgb FOREIGN KEY (rev) REFERENCES medflow.revinfo(rev);


--
-- Name: medico_especialidade_aud fk6xc3snmegnegpphx92ek2fe5v; Type: FK CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.medico_especialidade_aud
    ADD CONSTRAINT fk6xc3snmegnegpphx92ek2fe5v FOREIGN KEY (rev) REFERENCES medflow.revinfo(rev);


--
-- Name: usuario_aud fk74gdm3bhlqa3diq16ouihfq6e; Type: FK CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.usuario_aud
    ADD CONSTRAINT fk74gdm3bhlqa3diq16ouihfq6e FOREIGN KEY (rev) REFERENCES medflow.revinfo(rev);


--
-- Name: medico_especialidade fkbhtjhvqs7ga4awggpvewlvcc4; Type: FK CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.medico_especialidade
    ADD CONSTRAINT fkbhtjhvqs7ga4awggpvewlvcc4 FOREIGN KEY (medico_id) REFERENCES medflow.medico(id);


--
-- Name: medico_especialidade fkbt7xnmc3irfv6h8r9xymm03dp; Type: FK CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.medico_especialidade
    ADD CONSTRAINT fkbt7xnmc3irfv6h8r9xymm03dp FOREIGN KEY (especialidade_id) REFERENCES medflow.especialidade(id);


--
-- Name: medico_aud fkci10a8psk26fih807apirjml9; Type: FK CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.medico_aud
    ADD CONSTRAINT fkci10a8psk26fih807apirjml9 FOREIGN KEY (rev) REFERENCES medflow.revinfo(rev);


--
-- Name: consulta fkcjxfcmwtgp7yjhl5y1m9nm1fp; Type: FK CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.consulta
    ADD CONSTRAINT fkcjxfcmwtgp7yjhl5y1m9nm1fp FOREIGN KEY (consultorio_id) REFERENCES medflow.consultorio(id);


--
-- Name: alocacao_medico fkdeeewviuikn0j88e3h0gyye4g; Type: FK CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.alocacao_medico
    ADD CONSTRAINT fkdeeewviuikn0j88e3h0gyye4g FOREIGN KEY (medico_id) REFERENCES medflow.medico(id);


--
-- Name: alocacao_medico fkds77efxx0m7t2v11drh5oio9p; Type: FK CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.alocacao_medico
    ADD CONSTRAINT fkds77efxx0m7t2v11drh5oio9p FOREIGN KEY (consultorio_id) REFERENCES medflow.consultorio(id);


--
-- Name: registro_atendimento fkfdb8i8pu82nlc7vxhdy0gmbmd; Type: FK CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.registro_atendimento
    ADD CONSTRAINT fkfdb8i8pu82nlc7vxhdy0gmbmd FOREIGN KEY (medico_id) REFERENCES medflow.medico(id);


--
-- Name: agenda_medica_aud fkg473vctfkl1y3req2c7uwokby; Type: FK CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.agenda_medica_aud
    ADD CONSTRAINT fkg473vctfkl1y3req2c7uwokby FOREIGN KEY (rev) REFERENCES medflow.revinfo(rev);


--
-- Name: bloqueio_agenda fkgfsw4gjgty3vtddy3mw659yfh; Type: FK CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.bloqueio_agenda
    ADD CONSTRAINT fkgfsw4gjgty3vtddy3mw659yfh FOREIGN KEY (medico_id) REFERENCES medflow.medico(id);


--
-- Name: consultorio_aud fkiijm5ta24xff6gcn7ubquudsh; Type: FK CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.consultorio_aud
    ADD CONSTRAINT fkiijm5ta24xff6gcn7ubquudsh FOREIGN KEY (rev) REFERENCES medflow.revinfo(rev);


--
-- Name: unidade_aud fkj2jf53dofwo3ahh3xrmi4lxgt; Type: FK CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.unidade_aud
    ADD CONSTRAINT fkj2jf53dofwo3ahh3xrmi4lxgt FOREIGN KEY (rev) REFERENCES medflow.revinfo(rev);


--
-- Name: anexo_consulta_aud fkjo0iifi9r67gdnfs4tuaij7t8; Type: FK CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.anexo_consulta_aud
    ADD CONSTRAINT fkjo0iifi9r67gdnfs4tuaij7t8 FOREIGN KEY (rev) REFERENCES medflow.revinfo(rev);


--
-- Name: anexo_consulta fkk5j36ydr71qbhajvtuyk9rodm; Type: FK CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.anexo_consulta
    ADD CONSTRAINT fkk5j36ydr71qbhajvtuyk9rodm FOREIGN KEY (consulta_id) REFERENCES medflow.consulta(id);


--
-- Name: usuario fkofn2s20wpd6ypsscesejd128w; Type: FK CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.usuario
    ADD CONSTRAINT fkofn2s20wpd6ypsscesejd128w FOREIGN KEY (organizacao_id) REFERENCES medflow.organizacao(id);


--
-- Name: consulta fkoy3wjapl0q0l4xwrj83kmrm5q; Type: FK CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.consulta
    ADD CONSTRAINT fkoy3wjapl0q0l4xwrj83kmrm5q FOREIGN KEY (alocacao_medico_id) REFERENCES medflow.alocacao_medico(id);


--
-- Name: consulta fkpe2osbn1dt0kdfxtb0fqyklkj; Type: FK CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.consulta
    ADD CONSTRAINT fkpe2osbn1dt0kdfxtb0fqyklkj FOREIGN KEY (usuario_id) REFERENCES medflow.usuario(id);


--
-- Name: especialidade_aud fkpsrup3drwyni05dmmqecixc70; Type: FK CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.especialidade_aud
    ADD CONSTRAINT fkpsrup3drwyni05dmmqecixc70 FOREIGN KEY (rev) REFERENCES medflow.revinfo(rev);


--
-- Name: consulta_aud fkq9mt009d3tvbpx0b2v0sha70y; Type: FK CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.consulta_aud
    ADD CONSTRAINT fkq9mt009d3tvbpx0b2v0sha70y FOREIGN KEY (rev) REFERENCES medflow.revinfo(rev);


--
-- Name: agenda_medica fkr90wcglecf2fuxrvv1a3jn7qs; Type: FK CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.agenda_medica
    ADD CONSTRAINT fkr90wcglecf2fuxrvv1a3jn7qs FOREIGN KEY (alocacao_medico_id) REFERENCES medflow.alocacao_medico(id);


--
-- Name: registro_atendimento fkscx3w6e9y4ivcask1msnuct74; Type: FK CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.registro_atendimento
    ADD CONSTRAINT fkscx3w6e9y4ivcask1msnuct74 FOREIGN KEY (consulta_id) REFERENCES medflow.consulta(id);


--
-- Name: consulta fkskwvuuev2optdrjsfeo1pqo83; Type: FK CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.consulta
    ADD CONSTRAINT fkskwvuuev2optdrjsfeo1pqo83 FOREIGN KEY (medico_id) REFERENCES medflow.medico(id);


--
-- Name: unidade fksx1h4smpc4ibh3svifntyr1wo; Type: FK CONSTRAINT; Schema: medflow; Owner: -
--

ALTER TABLE ONLY medflow.unidade
    ADD CONSTRAINT fksx1h4smpc4ibh3svifntyr1wo FOREIGN KEY (organizacao_id) REFERENCES medflow.organizacao(id);


--
-- PostgreSQL database dump complete
--


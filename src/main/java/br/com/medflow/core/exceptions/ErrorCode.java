package br.com.medflow.core.exceptions;

/**
 * Códigos padronizados de erro da API Medflow.
 *
 * <p>
 * Cada código identifica de forma única uma condição de erro
 * que o frontend pode tratar programaticamente.
 */
public enum ErrorCode {

    // ---- Autenticação / Autorização ----
    /** Usuário autenticado não possui vínculo local no Medflow. */
    USER_NOT_LINKED,
    /** Usuário está inativo no Medflow. */
    USER_INACTIVE,
    /** Organização do usuário está inativa. */
    ORGANIZATION_INACTIVE,
    /** Permissão funcional negada pelo Keycloak ou regra contextual. */
    ACCESS_DENIED,

    // ---- Agendamento ----
    /** Horário selecionado não está mais disponível (conflito). */
    HORARIO_INDISPONIVEL,
    /** Médico não está disponível para o horário solicitado. */
    MEDICO_INDISPONIVEL,
    /** Consultório não está disponível para o horário solicitado. */
    CONSULTORIO_INDISPONIVEL,

    // ---- Transições de consulta ----
    /** Transição de status da consulta não é permitida. */
    TRANSICAO_CONSULTA_INVALIDA,
    /** Consulta não está no status esperado para a operação. */
    CONSULTA_STATUS_INVALIDO,

    // ---- Registro de atendimento ----
    /** Tentativa de registrar atendimento sem ser o médico da consulta. */
    REGISTRO_MEDICO_INVALIDO,
    /** Consulta não está em atendimento para registro. */
    REGISTRO_CONSULTA_NAO_EM_ATENDIMENTO,

    // ---- Validação / Recursos ----
    /** Erro genérico de validação de campos. */
    VALIDATION_ERROR,
    /** Recurso solicitado não encontrado. */
    RESOURCE_NOT_FOUND,

    // ---- Genérico ----
    /** Erro interno inesperado. */
    INTERNAL_ERROR
}

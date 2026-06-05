package br.com.medflow.schemas.organizacao;

import java.util.UUID;

/**
 * Schema de saída para organizações.
 *
 * @param id identificador da organização
 * @param nome nome da organização
 * @param email e-mail da organização
 * @param telefone telefone da organização
 * @param logotipo logotipo da organização
 * @param logotipoContentType content type do logotipo
 * @param corPrimaria cor primária da organização
 * @param ativo indicador de atividade
 */
public record OrganizacaoOutput(
    UUID id,
    String nome,
    String email,
    String telefone,
    byte[] logotipo,
    String logotipoContentType,
    String corPrimaria,
    boolean ativo) {
}

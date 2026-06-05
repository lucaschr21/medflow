package br.com.medflow.schemas.unidade;

import java.util.UUID;

import br.com.medflow.schemas.common.EnderecoOutput;

/**
 * Schema de saída para unidades.
 *
 * @param id identificador da unidade
 * @param organizacaoId identificador da organização
 * @param nome nome da unidade
 * @param telefone telefone da unidade
 * @param endereco endereço da unidade
 * @param ativo indicador de atividade
 */
public record UnidadeOutput(
    UUID id,
    UUID organizacaoId,
    String nome,
    String telefone,
    EnderecoOutput endereco,
    boolean ativo) {
}

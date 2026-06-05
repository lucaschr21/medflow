package br.com.medflow.schemas.common;

import br.com.medflow.entities.enums.Uf;

/**
 * Schema de saída para endereços.
 *
 * @param logradouro logradouro do endereço
 * @param numero número do endereço
 * @param bairro bairro do endereço
 * @param cidade cidade do endereço
 * @param uf unidade federativa do endereço
 * @param cep cep do endereço
 * @param complemento complemento do endereço
 */
public record EnderecoOutput(
    String logradouro,
    String numero,
    String bairro,
    String cidade,
    Uf uf,
    String cep,
    String complemento) {
}

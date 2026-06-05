package br.com.medflow.schemas.anexoconsulta;

import java.util.UUID;

/**
 * Schema de saída para anexos de consulta.
 *
 * @param id identificador do anexo
 * @param consultaId identificador da consulta
 * @param nomeArquivo nome do arquivo
 * @param contentType content type do arquivo
 * @param tamanhoBytes tamanho do arquivo em bytes
 * @param descricao descrição do anexo
 */
public record AnexoConsultaOutput(
    UUID id,
    UUID consultaId,
    String nomeArquivo,
    String contentType,
    long tamanhoBytes,
    String descricao) {
}

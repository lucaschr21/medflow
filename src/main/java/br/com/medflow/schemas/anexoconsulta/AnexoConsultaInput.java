package br.com.medflow.schemas.anexoconsulta;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 * Schema de entrada para anexos de consulta.
 *
 * @param consultaId identificador da consulta
 * @param nomeArquivo nome do arquivo
 * @param contentType content type do arquivo
 * @param tamanhoBytes tamanho do arquivo em bytes
 * @param arquivo conteúdo binário do arquivo
 * @param descricao descrição do anexo
 */
public record AnexoConsultaInput(
    @NotNull(message = "A consulta do anexo é obrigatória.")
    UUID consultaId,
    @NotBlank(message = "O nome do arquivo é obrigatório.")
    @Size(max = 255, message = "O nome do arquivo deve ter no máximo 255 caracteres.")
    String nomeArquivo,
    @NotBlank(message = "O content type do arquivo é obrigatório.")
    @Size(max = 100, message = "O content type do arquivo deve ter no máximo 100 caracteres.")
    String contentType,
    @PositiveOrZero(message = "O tamanho do arquivo não pode ser negativo.")
    long tamanhoBytes,
    @NotNull(message = "O arquivo do anexo é obrigatório.")
    byte[] arquivo,
    @Size(max = 500, message = "A descrição do anexo deve ter no máximo 500 caracteres.")
    String descricao) {
}

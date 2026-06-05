package br.com.medflow.entities;

import org.hibernate.envers.Audited;

import br.com.medflow.core.audit.Auditable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.CheckConstraint;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa um anexo binario associado a uma consulta.
 */
@Getter
@Setter
@Entity
@Audited
@Table(
    name = "anexo_consulta",
    indexes = @Index(name = "ix_anexo_consulta_consulta_id", columnList = "consulta_id"),
    check = @CheckConstraint(
        name = "ck_anexo_consulta_dados",
        constraint = "tamanho_bytes >= 0 and char_length(trim(nome_arquivo)) > 0 and char_length(trim(content_type)) > 0"))
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class AnexoConsulta extends Auditable {

  @NotNull(message = "A consulta do anexo é obrigatória.")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "consulta_id", nullable = false)
  private Consulta consulta;

  @NotBlank(message = "O nome do arquivo é obrigatório.")
  @Size(max = 255, message = "O nome do arquivo deve ter no máximo 255 caracteres.")
  @Column(name = "nome_arquivo", nullable = false, length = 255)
  private String nomeArquivo;

  @NotBlank(message = "O content type do arquivo é obrigatório.")
  @Size(max = 100, message = "O content type do arquivo deve ter no máximo 100 caracteres.")
  @Column(name = "content_type", nullable = false, length = 100)
  private String contentType;

  @PositiveOrZero(message = "O tamanho do arquivo não pode ser negativo.")
  @Column(name = "tamanho_bytes", nullable = false)
  private long tamanhoBytes;

  @Lob
  @Basic(fetch = FetchType.LAZY)
  @NotNull(message = "O arquivo do anexo é obrigatório.")
  @Column(name = "arquivo", nullable = false)
  private byte[] arquivo;

  @Size(max = 500, message = "A descrição do anexo deve ter no máximo 500 caracteres.")
  @Column(name = "descricao", length = 500)
  private String descricao;
}

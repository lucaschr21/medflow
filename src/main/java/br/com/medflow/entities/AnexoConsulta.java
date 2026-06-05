package br.com.medflow.entities;

import org.hibernate.envers.Audited;

import br.com.medflow.core.audit.Auditable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "anexo_consulta")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class AnexoConsulta extends Auditable {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "consulta_id", nullable = false)
  private Consulta consulta;

  @Column(name = "nome_arquivo", nullable = false)
  private String nomeArquivo;

  @Column(name = "content_type", nullable = false)
  private String contentType;

  @Column(name = "tamanho_bytes", nullable = false)
  private long tamanhoBytes;

  @Lob
  @Basic(fetch = FetchType.LAZY)
  @Column(name = "arquivo", nullable = false)
  private byte[] arquivo;

  @Column(name = "descricao")
  private String descricao;
}

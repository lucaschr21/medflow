package br.com.medflow.entities.atendimento;

import static br.com.medflow.entities.base.DomainValidation.optionalText;
import static br.com.medflow.entities.base.DomainValidation.required;

import java.time.LocalDate;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import br.com.medflow.entities.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "documento_medico")
public class DocumentoMedico extends BaseEntity {

  @NotNull(message = "Tipo de documento é obrigatório")
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TipoDocumento tipo;

  @NotNull(message = "Data de emissão é obrigatória")
  @Column(name = "data_emissao", nullable = false)
  private LocalDate dataEmissao;

  @Column(name = "assinatura_digital", columnDefinition = "text")
  private String assinaturaDigital;

  @NotNull(message = "Consulta é obrigatória")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "consulta_id", nullable = false)
  private Consulta consulta;

  public void definirTipo(TipoDocumento tipo) {
    this.tipo = required(tipo, "Tipo de documento é obrigatório");
  }

  public void definirDataEmissao(LocalDate dataEmissao) {
    this.dataEmissao = required(dataEmissao, "Data de emissão é obrigatória");
  }

  public void definirAssinaturaDigital(String assinaturaDigital) {
    this.assinaturaDigital = optionalText(assinaturaDigital);
  }

  void setConsulta(Consulta consulta) {
    this.consulta = required(consulta, "Consulta é obrigatória");
  }
}

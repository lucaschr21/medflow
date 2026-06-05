package br.com.medflow.entities;

import org.hibernate.envers.Audited;

import br.com.medflow.core.audit.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CheckConstraint;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa o registro clinico produzido durante o atendimento de uma consulta.
 */
@Getter
@Setter
@Entity
@Audited
@Table(
    name = "registro_atendimento",
    indexes = {
        @Index(name = "ix_registro_atendimento_consulta_id", columnList = "consulta_id"),
        @Index(name = "ix_registro_atendimento_medico_id", columnList = "medico_id")
    },
    check = @CheckConstraint(
        name = "ck_registro_atendimento_campos_textuais",
        constraint = "char_length(trim(queixa_principal)) > 0 and char_length(trim(anamnese)) > 0 and char_length(trim(conduta)) > 0"))
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class RegistroAtendimento extends Auditable {

  @NotNull(message = "A consulta do registro de atendimento é obrigatória.")
  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "consulta_id", nullable = false, unique = true)
  private Consulta consulta;

  @NotNull(message = "O médico do registro de atendimento é obrigatório.")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "medico_id", nullable = false)
  private Medico medico;

  @NotBlank(message = "A queixa principal é obrigatória.")
  @Size(max = 500, message = "A queixa principal deve ter no máximo 500 caracteres.")
  @Column(name = "queixa_principal", nullable = false, length = 500)
  private String queixaPrincipal;

  @Lob
  @NotBlank(message = "A anamnese é obrigatória.")
  @Column(name = "anamnese", nullable = false)
  private String anamnese;

  @Lob
  @NotBlank(message = "A conduta é obrigatória.")
  @Column(name = "conduta", nullable = false)
  private String conduta;

  @Lob
  @Size(max = 10000, message = "As observações devem ter no máximo 10000 caracteres.")
  @Column(name = "observacoes")
  private String observacoes;
}

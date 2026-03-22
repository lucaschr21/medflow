package br.com.medflow.entities.estrutura;

import br.com.medflow.entities.base.BaseEntity;
import br.com.medflow.entities.pessoas.Medico;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(
    name = "consultorio_medico",
    uniqueConstraints =
        @UniqueConstraint(
            name = "uk_consultorio_medico",
            columnNames = {"consultorio_id", "medico_id"}),
    indexes = {@Index(name = "idx_consultorio_medico_medico", columnList = "medico_id")})
public class ConsultorioMedico extends BaseEntity {

  @NotNull(message = "Consultório é obrigatório")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "consultorio_id", nullable = false)
  private Consultorio consultorio;

  @NotNull(message = "Médico é obrigatório")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "medico_id", nullable = false)
  private Medico medico;

  public ConsultorioMedico(Consultorio consultorio, Medico medico) {
    this.consultorio = Objects.requireNonNull(consultorio, "Consultório é obrigatório");
    this.medico = Objects.requireNonNull(medico, "Médico é obrigatório");
  }
}

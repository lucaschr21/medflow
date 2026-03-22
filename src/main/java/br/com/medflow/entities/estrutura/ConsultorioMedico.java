package br.com.medflow.entities.estrutura;

import static br.com.medflow.entities.base.DomainValidation.requireNotBefore;
import static br.com.medflow.entities.base.DomainValidation.required;

import br.com.medflow.entities.agenda.Agenda;
import br.com.medflow.entities.base.BaseEntity;
import br.com.medflow.entities.pessoas.Medico;
import jakarta.persistence.CheckConstraint;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
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
    check = @CheckConstraint(constraint = "data_fim IS NULL OR data_fim >= data_inicio"),
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

  @SuppressWarnings("OneToOneWithLazy")
  @OneToOne(mappedBy = "vinculacaoConsultorioMedico", fetch = FetchType.LAZY)
  private Agenda agenda;

  @NotNull(message = "Data de início da vigência é obrigatória")
  @Column(name = "data_inicio", nullable = false)
  private LocalDate dataInicio;

  @Column(name = "data_fim")
  private LocalDate dataFim;

  public ConsultorioMedico(Consultorio consultorio, Medico medico) {
    this.consultorio = required(consultorio, "Consultório é obrigatório");
    this.medico = required(medico, "Médico é obrigatório");
    this.dataInicio = LocalDate.now();
  }

  public void definirDataInicio(LocalDate dataInicio) {
    this.dataInicio = required(dataInicio, "Data de início da vigência é obrigatória");
    requireNotBefore(
        this.dataFim,
        this.dataInicio,
        "Data fim da vigência não pode ser anterior à data de início");
  }

  public void definirDataFim(LocalDate dataFim) {
    requireNotBefore(
        dataFim, this.dataInicio, "Data fim da vigência não pode ser anterior à data de início");
    this.dataFim = dataFim;
  }

  public boolean estaVinculoAtivoEm(LocalDate data) {
    LocalDate referencia = required(data, "Data de referência é obrigatória");
    boolean iniciou = !referencia.isBefore(this.dataInicio);
    boolean naoTerminou = this.dataFim == null || !referencia.isAfter(this.dataFim);
    return iniciou && naoTerminou;
  }
}

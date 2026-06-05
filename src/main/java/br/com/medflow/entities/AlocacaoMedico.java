package br.com.medflow.entities;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.envers.Audited;

import br.com.medflow.core.audit.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CheckConstraint;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa a alocacao de um medico em um consultorio por um periodo.
 */
@Getter
@Setter
@Entity
@Audited
@Table(
    name = "alocacao_medico",
    indexes = {
        @Index(name = "ix_alocacao_medico_medico_id", columnList = "medico_id"),
        @Index(name = "ix_alocacao_medico_consultorio_id", columnList = "consultorio_id"),
        @Index(name = "ix_alocacao_medico_medico_data_inicio", columnList = "medico_id, data_inicio"),
        @Index(name = "ix_alocacao_medico_consultorio_data_inicio", columnList = "consultorio_id, data_inicio")
    },
    check = @CheckConstraint(
        name = "ck_alocacao_medico_periodo",
        constraint = "data_fim is null or data_fim >= data_inicio"))
@NoArgsConstructor
public class AlocacaoMedico extends Auditable {

  @NotNull(message = "O médico da alocação é obrigatório.")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "medico_id", nullable = false)
  private Medico medico;

  @NotNull(message = "O consultório da alocação é obrigatório.")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "consultorio_id", nullable = false)
  private Consultorio consultorio;

  @NotNull(message = "A data de início da alocação é obrigatória.")
  @Column(name = "data_inicio", nullable = false)
  private LocalDate dataInicio;

  @Column(name = "data_fim")
  private LocalDate dataFim;

  @ColumnDefault("true")
  @Column(name = "ativo", nullable = false)
  private boolean ativo = true;

  @Setter(lombok.AccessLevel.NONE)
  @OneToMany(mappedBy = "alocacaoMedico")
  private Set<AgendaMedica> agendasMedicas = new LinkedHashSet<>();

  @Setter(lombok.AccessLevel.NONE)
  @OneToMany(mappedBy = "alocacaoMedico")
  private Set<Consulta> consultas = new LinkedHashSet<>();
}

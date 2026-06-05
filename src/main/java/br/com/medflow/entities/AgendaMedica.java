package br.com.medflow.entities;

import java.time.DayOfWeek;
import java.time.LocalTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.envers.Audited;
import org.hibernate.type.SqlTypes;

import br.com.medflow.core.audit.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.CheckConstraint;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa uma faixa recorrente de agenda associada a uma alocacao de medico.
 */
@Getter
@Setter
@Entity
@Audited
@Table(
    name = "agenda_medica",
    indexes = {
        @Index(name = "ix_agenda_medica_alocacao_medico_id", columnList = "alocacao_medico_id"),
        @Index(name = "ix_agenda_medica_alocacao_dia_semana", columnList = "alocacao_medico_id, dia_semana")
    },
    uniqueConstraints = @UniqueConstraint(
        name = "uk_agenda_medica_alocacao_dia_horas",
        columnNames = { "alocacao_medico_id", "dia_semana", "hora_inicio", "hora_fim" }),
    check = @CheckConstraint(name = "ck_agenda_medica_intervalo", constraint = "hora_fim > hora_inicio"))
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class AgendaMedica extends Auditable {

  @NotNull(message = "A alocação médica da agenda é obrigatória.")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "alocacao_medico_id", nullable = false)
  private AlocacaoMedico alocacaoMedico;

  @NotNull(message = "O dia da semana da agenda é obrigatório.")
  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(name = "dia_semana", nullable = false)
  private DayOfWeek diaSemana;

  @NotNull(message = "A hora de início da agenda é obrigatória.")
  @Column(name = "hora_inicio", nullable = false)
  private LocalTime horaInicio;

  @NotNull(message = "A hora de fim da agenda é obrigatória.")
  @Column(name = "hora_fim", nullable = false)
  private LocalTime horaFim;

  @ColumnDefault("true")
  @Column(name = "ativo", nullable = false)
  private boolean ativo = true;
}

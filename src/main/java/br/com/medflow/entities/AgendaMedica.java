package br.com.medflow.entities;

import java.time.DayOfWeek;
import java.time.LocalTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.envers.Audited;
import org.hibernate.type.SqlTypes;

import br.com.medflow.core.audit.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "agenda_medica")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class AgendaMedica extends Auditable {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "alocacao_medico_id", nullable = false)
  private AlocacaoMedico alocacaoMedico;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(name = "dia_semana", nullable = false)
  private DayOfWeek diaSemana;

  @Column(name = "hora_inicio", nullable = false)
  private LocalTime horaInicio;

  @Column(name = "hora_fim", nullable = false)
  private LocalTime horaFim;

  @ColumnDefault("true")
  @Column(name = "ativo", nullable = false)
  private boolean ativo = true;
}

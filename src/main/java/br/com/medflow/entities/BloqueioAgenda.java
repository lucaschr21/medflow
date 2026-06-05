package br.com.medflow.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.envers.Audited;
import org.hibernate.type.SqlTypes;

import br.com.medflow.core.audit.Auditable;
import br.com.medflow.entities.enums.TipoBloqueioAgenda;
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
 * Representa um bloqueio de agenda de um medico em determinado consultorio.
 */
@Getter
@Setter
@Entity
@Audited
@Table(name = "bloqueio_agenda")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class BloqueioAgenda extends Auditable {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "medico_id", nullable = false)
  private Medico medico;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "consultorio_id", nullable = false)
  private Consultorio consultorio;

  @Column(name = "inicio", nullable = false)
  private LocalDateTime inicio;

  @Column(name = "fim", nullable = false)
  private LocalDateTime fim;

  @Column(name = "motivo", nullable = false)
  private String motivo;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(name = "tipo", nullable = false)
  private TipoBloqueioAgenda tipo;
}

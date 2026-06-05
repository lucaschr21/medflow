package br.com.medflow.entities;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.envers.Audited;
import org.hibernate.type.SqlTypes;

import br.com.medflow.core.audit.Auditable;
import br.com.medflow.entities.enums.StatusConsulta;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa uma consulta agendada para atendimento medico.
 */
@Getter
@Setter
@Entity
@Audited
@Table(name = "consulta")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Consulta extends Auditable {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "usuario_id", nullable = false)
  private Usuario usuario;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "medico_id", nullable = false)
  private Medico medico;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "consultorio_id", nullable = false)
  private Consultorio consultorio;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "alocacao_medico_id", nullable = false)
  private AlocacaoMedico alocacaoMedico;

  @Column(name = "data_hora_inicio", nullable = false)
  private LocalDateTime dataHoraInicio;

  @Column(name = "data_hora_fim", nullable = false)
  private LocalDateTime dataHoraFim;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(name = "status", nullable = false)
  private StatusConsulta status;

  @Column(name = "tipo_consulta", nullable = false)
  private String tipoConsulta;

  @Column(name = "motivo", nullable = false)
  private String motivo;

  @OneToOne(mappedBy = "consulta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private RegistroAtendimento registroAtendimento;

  @Setter(lombok.AccessLevel.NONE)
  @OneToMany(mappedBy = "consulta", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<AnexoConsulta> anexos = new LinkedHashSet<>();
}

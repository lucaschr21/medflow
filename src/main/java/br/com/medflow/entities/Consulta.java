package br.com.medflow.entities;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.envers.Audited;
import org.hibernate.type.SqlTypes;

import br.com.medflow.core.audit.Auditable;
import br.com.medflow.entities.enums.StatusConsulta;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
 * Representa uma consulta agendada para atendimento medico.
 */
@Getter
@Setter
@Entity
@Audited
@Table(
    name = "consulta",
    indexes = {
        @Index(name = "ix_consulta_usuario_id", columnList = "usuario_id"),
        @Index(name = "ix_consulta_medico_id", columnList = "medico_id"),
        @Index(name = "ix_consulta_consultorio_id", columnList = "consultorio_id"),
        @Index(name = "ix_consulta_alocacao_medico_id", columnList = "alocacao_medico_id"),
        @Index(name = "ix_consulta_medico_data_hora_inicio", columnList = "medico_id, data_hora_inicio"),
        @Index(name = "ix_consulta_consultorio_data_hora_inicio", columnList = "consultorio_id, data_hora_inicio"),
        @Index(name = "ix_consulta_usuario_data_hora_inicio", columnList = "usuario_id, data_hora_inicio")
    },
    check = @CheckConstraint(
        name = "ck_consulta_intervalo_campos_textuais",
        constraint = "data_hora_fim > data_hora_inicio and char_length(trim(tipo_consulta)) > 0 and char_length(trim(motivo)) > 0"))
@NoArgsConstructor
public class Consulta extends Auditable {

  @NotNull(message = "O usuário da consulta é obrigatório.")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "usuario_id", nullable = false)
  private Usuario usuario;

  @NotNull(message = "O médico da consulta é obrigatório.")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "medico_id", nullable = false)
  private Medico medico;

  @NotNull(message = "O consultório da consulta é obrigatório.")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "consultorio_id", nullable = false)
  private Consultorio consultorio;

  @NotNull(message = "A alocação médica da consulta é obrigatória.")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "alocacao_medico_id", nullable = false)
  private AlocacaoMedico alocacaoMedico;

  @NotNull(message = "A data e hora de início da consulta é obrigatória.")
  @Column(name = "data_hora_inicio", nullable = false)
  private LocalDateTime dataHoraInicio;

  @NotNull(message = "A data e hora de fim da consulta é obrigatória.")
  @Column(name = "data_hora_fim", nullable = false)
  private LocalDateTime dataHoraFim;

  @NotNull(message = "O status da consulta é obrigatório.")
  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(name = "status", nullable = false)
  private StatusConsulta status;

  @NotBlank(message = "O tipo da consulta é obrigatório.")
  @Size(max = 80, message = "O tipo da consulta deve ter no máximo 80 caracteres.")
  @Column(name = "tipo_consulta", nullable = false, length = 80)
  private String tipoConsulta;

  @NotBlank(message = "O motivo da consulta é obrigatório.")
  @Size(max = 500, message = "O motivo da consulta deve ter no máximo 500 caracteres.")
  @Column(name = "motivo", nullable = false, length = 500)
  private String motivo;

  @OneToOne(mappedBy = "consulta", fetch = FetchType.LAZY)
  private RegistroAtendimento registroAtendimento;

  @Setter(lombok.AccessLevel.NONE)
  @OneToMany(mappedBy = "consulta")
  private Set<AnexoConsulta> anexos = new LinkedHashSet<>();
}

package br.com.medflow.entities.agenda;

import br.com.medflow.entities.base.BaseEntity;
import br.com.medflow.entities.estrutura.ConsultorioMedico;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "agenda")
public class Agenda extends BaseEntity {

  @ElementCollection(fetch = FetchType.LAZY)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Enumerated(EnumType.STRING)
  @CollectionTable(
      name = "agenda_horario_trabalho",
      joinColumns = @JoinColumn(name = "agenda_id"),
      uniqueConstraints =
          @UniqueConstraint(
              name = "uk_agenda_dia_semana",
              columnNames = {"agenda_id", "dia_semana"}))
  @Column(name = "dia_semana", nullable = false, length = 16)
  private Set<@NotNull(message = "Dia de trabalho é obrigatório") DayOfWeek> horarioTrabalho =
      new LinkedHashSet<>();

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(
      name = "agenda_bloqueio",
      joinColumns = @JoinColumn(name = "agenda_id"),
      indexes =
          @Index(
              name = "idx_agenda_bloqueio_agenda_inicio",
              columnList = "agenda_id, data_hora_inicio"))
  @AttributeOverrides({
    @AttributeOverride(
        name = "inicio",
        column = @Column(name = "data_hora_inicio", nullable = false)),
    @AttributeOverride(name = "fim", column = @Column(name = "data_hora_fim", nullable = false))
  })
  private Set<@Valid BloqueioAgenda> bloqueios = new LinkedHashSet<>();

  @NotNull(message = "Vínculo consultório-médico é obrigatório")
  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "consultorio_medico_id", nullable = false, unique = true)
  private ConsultorioMedico vinculacaoConsultorioMedico;

  public void definirVinculacaoConsultorioMedico(ConsultorioMedico vinculacaoConsultorioMedico) {
    this.vinculacaoConsultorioMedico =
        Objects.requireNonNull(
            vinculacaoConsultorioMedico, "Vínculo consultório-médico é obrigatório");
  }

  public void adicionarDiaTrabalho(DayOfWeek dia) {
    this.horarioTrabalho.add(Objects.requireNonNull(dia, "Dia de trabalho é obrigatório"));
  }

  public void removerDiaTrabalho(DayOfWeek dia) {
    this.horarioTrabalho.remove(Objects.requireNonNull(dia, "Dia de trabalho é obrigatório"));
  }

  public void adicionarBloqueio(LocalDateTime inicio, LocalDateTime fim) {
    this.adicionarBloqueio(inicio, fim, EscopoBloqueioAgenda.MEDICO);
  }

  public void adicionarBloqueio(
      LocalDateTime inicio, LocalDateTime fim, EscopoBloqueioAgenda escopo) {
    this.bloqueios.add(new BloqueioAgenda(inicio, fim, escopo));
  }

  public void adicionarBloqueioMedico(LocalDateTime inicio, LocalDateTime fim) {
    this.adicionarBloqueio(inicio, fim, EscopoBloqueioAgenda.MEDICO);
  }

  public void adicionarBloqueioConsultorio(LocalDateTime inicio, LocalDateTime fim) {
    this.adicionarBloqueio(inicio, fim, EscopoBloqueioAgenda.CONSULTORIO);
  }

  public void removerBloqueio(LocalDateTime inicio, LocalDateTime fim) {
    this.removerBloqueio(inicio, fim, EscopoBloqueioAgenda.MEDICO);
  }

  public void removerBloqueio(
      LocalDateTime inicio, LocalDateTime fim, EscopoBloqueioAgenda escopo) {
    this.bloqueios.remove(new BloqueioAgenda(inicio, fim, escopo));
  }

  public void removerBloqueioMedico(LocalDateTime inicio, LocalDateTime fim) {
    this.removerBloqueio(inicio, fim, EscopoBloqueioAgenda.MEDICO);
  }

  public void removerBloqueioConsultorio(LocalDateTime inicio, LocalDateTime fim) {
    this.removerBloqueio(inicio, fim, EscopoBloqueioAgenda.CONSULTORIO);
  }
}

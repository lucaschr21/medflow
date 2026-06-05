package br.com.medflow.entities;

import java.util.LinkedHashSet;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.envers.Audited;

import br.com.medflow.core.audit.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa um médico do Medflow.
 */
@Getter
@Setter
@Entity
@Audited
@Table(name = "medico")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Medico extends Auditable {

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "usuario_id", nullable = false, unique = true)
  private Usuario usuario;

  @ColumnDefault("true")
  @Column(name = "ativo", nullable = false)
  private boolean ativo = true;

  @Setter(lombok.AccessLevel.NONE)
  @ManyToMany
  @JoinTable(
      name = "medico_especialidade",
      uniqueConstraints = @UniqueConstraint(name = "uk_medico_especialidade", columnNames = { "medico_id",
          "especialidade_id" }),
      joinColumns = @JoinColumn(name = "medico_id", nullable = false),
      inverseJoinColumns = @JoinColumn(name = "especialidade_id", nullable = false))
  private Set<Especialidade> especialidades = new LinkedHashSet<>();

  @Setter(lombok.AccessLevel.NONE)
  @OneToMany(mappedBy = "medico")
  private Set<AlocacaoMedico> alocacoesMedico = new LinkedHashSet<>();

  @Setter(lombok.AccessLevel.NONE)
  @OneToMany(mappedBy = "medico")
  private Set<BloqueioAgenda> bloqueiosAgenda = new LinkedHashSet<>();

  @Setter(lombok.AccessLevel.NONE)
  @OneToMany(mappedBy = "medico")
  private Set<Consulta> consultas = new LinkedHashSet<>();

  @Setter(lombok.AccessLevel.NONE)
  @OneToMany(mappedBy = "medico")
  private Set<RegistroAtendimento> registrosAtendimento = new LinkedHashSet<>();
}

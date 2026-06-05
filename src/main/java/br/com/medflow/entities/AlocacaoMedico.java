package br.com.medflow.entities;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.envers.Audited;

import br.com.medflow.core.audit.Auditable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "alocacao_medico")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class AlocacaoMedico extends Auditable {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "medico_id", nullable = false)
  private Medico medico;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "consultorio_id", nullable = false)
  private Consultorio consultorio;

  @Column(name = "data_inicio", nullable = false)
  private LocalDate dataInicio;

  @Column(name = "data_fim")
  private LocalDate dataFim;

  @ColumnDefault("true")
  @Column(name = "ativo", nullable = false)
  private boolean ativo = true;

  @Setter(lombok.AccessLevel.NONE)
  @OneToMany(mappedBy = "alocacaoMedico", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<AgendaMedica> agendasMedicas = new LinkedHashSet<>();

  @Setter(lombok.AccessLevel.NONE)
  @OneToMany(mappedBy = "alocacaoMedico")
  private Set<Consulta> consultas = new LinkedHashSet<>();
}

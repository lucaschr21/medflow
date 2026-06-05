package br.com.medflow.entities;

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
 * Representa um consultório de uma unidade.
 */
@Getter
@Setter
@Entity
@Audited
@Table(name = "consultorio")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Consultorio extends Auditable {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "unidade_id", nullable = false)
  private Unidade unidade;

  @Column(name = "nome", nullable = false)
  private String nome;

  @Column(name = "sala", nullable = false)
  private String sala;

  @ColumnDefault("true")
  @Column(name = "ativo", nullable = false)
  private boolean ativo = true;

  @Setter(lombok.AccessLevel.NONE)
  @OneToMany(mappedBy = "consultorio", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<AlocacaoMedico> alocacoesMedico = new LinkedHashSet<>();

  @Setter(lombok.AccessLevel.NONE)
  @OneToMany(mappedBy = "consultorio", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<BloqueioAgenda> bloqueiosAgenda = new LinkedHashSet<>();

  @Setter(lombok.AccessLevel.NONE)
  @OneToMany(mappedBy = "consultorio")
  private Set<Consulta> consultas = new LinkedHashSet<>();
}

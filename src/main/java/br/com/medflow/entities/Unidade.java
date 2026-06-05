package br.com.medflow.entities;

import java.util.LinkedHashSet;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.envers.Audited;

import br.com.medflow.core.audit.Auditable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa uma unidade da organização.
 */
@Getter
@Setter
@Entity
@Audited
@Table(name = "unidade")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Unidade extends Auditable {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "organizacao_id", nullable = false)
  private Organizacao organizacao;

  @Column(name = "nome", nullable = false)
  private String nome;

  @Pattern(regexp = "\\d{10,11}")
  @Column(name = "telefone", nullable = false)
  private String telefone;

  @ColumnDefault("true")
  @Column(name = "ativo", nullable = false)
  private boolean ativo = true;

  @Embedded
  private Endereco endereco;

  @Setter(lombok.AccessLevel.NONE)
  @OneToMany(mappedBy = "unidade", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Consultorio> consultorios = new LinkedHashSet<>();
}

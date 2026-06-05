package br.com.medflow.entities;

import java.util.LinkedHashSet;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.envers.Audited;

import br.com.medflow.core.audit.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa uma especialidade médica.
 */
@Getter
@Setter
@Entity
@Audited
@Table(name = "especialidade")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Especialidade extends Auditable {

  @Column(name = "nome", nullable = false)
  private String nome;

  @Column(name = "descricao")
  private String descricao;

  @ColumnDefault("true")
  @Column(name = "ativo", nullable = false)
  private boolean ativo = true;

  @Setter(lombok.AccessLevel.NONE)
  @ManyToMany(mappedBy = "especialidades")
  private Set<Medico> medicos = new LinkedHashSet<>();
}

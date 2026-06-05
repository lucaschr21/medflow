package br.com.medflow.entities;

import java.util.LinkedHashSet;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.envers.Audited;

import br.com.medflow.core.audit.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.CheckConstraint;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
@Table(
    name = "especialidade",
    check = @CheckConstraint(name = "ck_especialidade_nome", constraint = "char_length(trim(nome)) > 0"))
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Especialidade extends Auditable {

  @NotBlank(message = "O nome da especialidade é obrigatório.")
  @Size(max = 120, message = "O nome da especialidade deve ter no máximo 120 caracteres.")
  @Column(name = "nome", nullable = false, unique = true, length = 120)
  private String nome;

  @Size(max = 500, message = "A descrição da especialidade deve ter no máximo 500 caracteres.")
  @Column(name = "descricao", length = 500)
  private String descricao;

  @ColumnDefault("true")
  @Column(name = "ativo", nullable = false)
  private boolean ativo = true;

  @Setter(lombok.AccessLevel.NONE)
  @ManyToMany(mappedBy = "especialidades")
  private Set<Medico> medicos = new LinkedHashSet<>();
}

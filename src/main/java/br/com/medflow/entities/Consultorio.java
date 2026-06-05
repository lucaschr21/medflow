package br.com.medflow.entities;

import java.util.LinkedHashSet;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;
import org.hibernate.envers.Audited;

import br.com.medflow.core.audit.Auditable;
import jakarta.persistence.CheckConstraint;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@SoftDelete(strategy = SoftDeleteType.ACTIVE, columnName = "ativo")
@Table(name = "consultorio", indexes = @Index(name = "ix_consultorio_unidade_id", columnList = "unidade_id"), uniqueConstraints = @UniqueConstraint(name = "uk_consultorio_unidade_sala", columnNames = {
    "unidade_id",
    "sala" }), check = @CheckConstraint(name = "ck_consultorio_campos_textuais", constraint = "char_length(trim(nome)) > 0 and char_length(trim(sala)) > 0"))
@NoArgsConstructor
public class Consultorio extends Auditable {

  @NotNull(message = "A unidade do consultório é obrigatória.")
  @ManyToOne(optional = false)
  @JoinColumn(name = "unidade_id", nullable = false)
  private Unidade unidade;

  @NotBlank(message = "O nome do consultório é obrigatório.")
  @Size(max = 120, message = "O nome do consultório deve ter no máximo 120 caracteres.")
  @Column(name = "nome", nullable = false)
  private String nome;

  @NotBlank(message = "A sala do consultório é obrigatória.")
  @Size(max = 40, message = "A sala do consultório deve ter no máximo 40 caracteres.")
  @Column(name = "sala", nullable = false, length = 40)
  private String sala;

  @ColumnDefault("true")
  @Column(name = "ativo", nullable = false, insertable = false, updatable = false)
  private boolean ativo = true;

  @Setter(lombok.AccessLevel.NONE)
  @OneToMany(mappedBy = "consultorio")
  private Set<AlocacaoMedico> alocacoesMedico = new LinkedHashSet<>();

  @Setter(lombok.AccessLevel.NONE)
  @OneToMany(mappedBy = "consultorio")
  private Set<BloqueioAgenda> bloqueiosAgenda = new LinkedHashSet<>();

  @Setter(lombok.AccessLevel.NONE)
  @OneToMany(mappedBy = "consultorio")
  private Set<Consulta> consultas = new LinkedHashSet<>();
}

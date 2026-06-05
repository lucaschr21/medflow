package br.com.medflow.entities;

import java.util.LinkedHashSet;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.envers.Audited;

import br.com.medflow.core.audit.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CheckConstraint;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
@Table(
    name = "unidade",
    indexes = {
        @Index(name = "ix_unidade_organizacao_id", columnList = "organizacao_id"),
        @Index(name = "ix_unidade_organizacao_ativo", columnList = "organizacao_id, ativo")
    },
    uniqueConstraints = @UniqueConstraint(name = "uk_unidade_organizacao_nome", columnNames = { "organizacao_id", "nome" }),
    check = @CheckConstraint(
        name = "ck_unidade_campos_textuais",
        constraint = "char_length(trim(nome)) > 0 and char_length(trim(telefone)) > 0"))
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Unidade extends Auditable {

  @NotNull(message = "A organização da unidade é obrigatória.")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "organizacao_id", nullable = false)
  private Organizacao organizacao;

  @NotBlank(message = "O nome da unidade é obrigatório.")
  @Size(max = 120, message = "O nome da unidade deve ter no máximo 120 caracteres.")
  @Column(name = "nome", nullable = false)
  private String nome;

  @NotBlank(message = "O telefone da unidade é obrigatório.")
  @Pattern(regexp = "\\d{10,11}", message = "O telefone da unidade deve conter 10 ou 11 dígitos.")
  @Size(min = 10, max = 11, message = "O telefone da unidade deve conter entre 10 e 11 dígitos.")
  @Column(name = "telefone", nullable = false, length = 11)
  private String telefone;

  @ColumnDefault("true")
  @Column(name = "ativo", nullable = false)
  private boolean ativo = true;

  @Valid
  @NotNull(message = "O endereço da unidade é obrigatório.")
  @Embedded
  private Endereco endereco;

  @Setter(lombok.AccessLevel.NONE)
  @OneToMany(mappedBy = "unidade")
  private Set<Consultorio> consultorios = new LinkedHashSet<>();
}

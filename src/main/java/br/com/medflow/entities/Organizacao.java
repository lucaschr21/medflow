package br.com.medflow.entities;

import java.util.LinkedHashSet;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.envers.Audited;

import br.com.medflow.core.audit.Auditable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CheckConstraint;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa uma organização do Medflow.
 */
@Getter
@Setter
@Entity
@Audited
@Table(
    name = "organizacao",
    indexes = @Index(name = "ix_organizacao_ativo", columnList = "ativo"),
    check = @CheckConstraint(
        name = "ck_organizacao_campos_textuais",
        constraint = "char_length(trim(nome)) > 0 and char_length(trim(email)) > 0 and char_length(trim(telefone)) > 0 and char_length(trim(cor_primaria)) > 0"))
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Organizacao extends Auditable {

  @NotBlank(message = "O nome da organização é obrigatório.")
  @Size(max = 120, message = "O nome da organização deve ter no máximo 120 caracteres.")
  @Column(name = "nome", nullable = false)
  private String nome;

  @NotBlank(message = "O e-mail da organização é obrigatório.")
  @Email(message = "O e-mail da organização deve ser válido.")
  @Size(max = 254, message = "O e-mail da organização deve ter no máximo 254 caracteres.")
  @Column(name = "email", nullable = false)
  private String email;

  @NotBlank(message = "O telefone da organização é obrigatório.")
  @Pattern(regexp = "\\d{10,11}", message = "O telefone da organização deve conter 10 ou 11 dígitos.")
  @Size(min = 10, max = 11, message = "O telefone da organização deve conter entre 10 e 11 dígitos.")
  @Column(name = "telefone", nullable = false, length = 11)
  private String telefone;

  @Lob
  @Basic(fetch = FetchType.LAZY)
  @Column(name = "logotipo")
  private byte[] logotipo;

  @Size(max = 100, message = "O content type do logotipo deve ter no máximo 100 caracteres.")
  @Column(name = "logotipo_content_type")
  private String logotipoContentType;

  @NotBlank(message = "A cor primária da organização é obrigatória.")
  @Pattern(regexp = "#[0-9A-Fa-f]{6}", message = "A cor primária deve estar no formato hexadecimal #RRGGBB.")
  @Column(name = "cor_primaria", nullable = false, length = 7)
  private String corPrimaria;

  @ColumnDefault("true")
  @Column(name = "ativo", nullable = false)
  private boolean ativo = true;

  @Setter(lombok.AccessLevel.NONE)
  @OneToMany(mappedBy = "organizacao")
  private Set<Unidade> unidades = new LinkedHashSet<>();

  @Setter(lombok.AccessLevel.NONE)
  @OneToMany(mappedBy = "organizacao")
  private Set<Usuario> usuarios = new LinkedHashSet<>();
}

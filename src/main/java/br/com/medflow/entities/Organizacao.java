package br.com.medflow.entities;

import java.util.LinkedHashSet;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.envers.Audited;

import br.com.medflow.core.audit.Auditable;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
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
@Table(name = "organizacao")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Organizacao extends Auditable {

  @Column(name = "nome", nullable = false)
  private String nome;

  @Email
  @Column(name = "email", nullable = false)
  private String email;

  @Pattern(regexp = "\\d{10,11}")
  @Column(name = "telefone", nullable = false)
  private String telefone;

  @Lob
  @Basic(fetch = FetchType.LAZY)
  @Column(name = "logotipo")
  private byte[] logotipo;

  @Column(name = "logotipo_content_type")
  private String logotipoContentType;

  @Column(name = "cor_primaria", nullable = false)
  private String corPrimaria;

  @ColumnDefault("true")
  @Column(name = "ativo", nullable = false)
  private boolean ativo = true;

  @Setter(lombok.AccessLevel.NONE)
  @OneToMany(mappedBy = "organizacao", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Unidade> unidades = new LinkedHashSet<>();

  @Setter(lombok.AccessLevel.NONE)
  @OneToMany(mappedBy = "organizacao")
  private Set<Usuario> usuarios = new LinkedHashSet<>();
}

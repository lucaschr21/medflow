package br.com.medflow.entities;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;
import org.hibernate.envers.Audited;

import br.com.medflow.core.audit.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa o vínculo local do usuário autenticado com a organização.
 */
@Getter
@Setter
@Entity
@Audited
@SoftDelete(strategy = SoftDeleteType.ACTIVE, columnName = "ativo")
@Table(name = "usuario", indexes = {
    @Index(name = "ix_usuario_organizacao_id", columnList = "organizacao_id"),
    @Index(name = "ix_usuario_keycloak_id", columnList = "keycloak_id")
}, uniqueConstraints = @UniqueConstraint(name = "uk_usuario_organizacao_keycloak", columnNames = {
    "organizacao_id", "keycloak_id" }))
@NoArgsConstructor
public class Usuario extends Auditable {

  @NotNull(message = "A organização do usuário é obrigatória.")
  @ManyToOne(optional = false)
  @JoinColumn(name = "organizacao_id", nullable = false)
  private Organizacao organizacao;

  @NotNull(message = "O identificador do usuário no Keycloak é obrigatório.")
  @Column(name = "keycloak_id", nullable = false)
  private UUID keycloakId;

  @ColumnDefault("true")
  @Column(name = "ativo", nullable = false, insertable = false, updatable = false)
  private boolean ativo = true;

  @OneToOne(mappedBy = "usuario")
  private Medico medico;

  @Setter(lombok.AccessLevel.NONE)
  @OneToMany(mappedBy = "usuario")
  private Set<Consulta> consultas = new LinkedHashSet<>();
}

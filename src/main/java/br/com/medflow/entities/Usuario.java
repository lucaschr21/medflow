package br.com.medflow.entities;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.envers.Audited;

import br.com.medflow.core.audit.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(name = "usuario", uniqueConstraints = @UniqueConstraint(name = "uk_usuario_organizacao_keycloak", columnNames = {
    "organizacao_id", "keycloak_id" }))
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Usuario extends Auditable {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "organizacao_id", nullable = false)
  private Organizacao organizacao;

  @Column(name = "keycloak_id", nullable = false)
  private UUID keycloakId;

  @ColumnDefault("true")
  @Column(name = "ativo", nullable = false)
  private boolean ativo = true;

  @OneToOne(mappedBy = "usuario", fetch = FetchType.LAZY)
  private Medico medico;

  @Setter(lombok.AccessLevel.NONE)
  @OneToMany(mappedBy = "usuario")
  private Set<Consulta> consultas = new LinkedHashSet<>();
}

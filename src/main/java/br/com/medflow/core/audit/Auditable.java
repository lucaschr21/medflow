package br.com.medflow.core.audit;

import java.time.Instant;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

/**
 * Base comum de auditoria para entidades JPA do Medflow.
 *
 * <p>Esta superclasse centraliza os campos de auditoria de criação e última
 * alteração, sem definir {@code id} da entidade.
 *
 * <p>Exemplo de uso em uma entidade auditável:
 *
 * <pre>{@code
 * @Entity
 * @Audited
 * public class Usuario extends Auditable {
 *   ...
 * }
 * }</pre>
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {

  @CreatedBy
  @Column(name = "created_by", nullable = false, updatable = false)
  private String createdBy;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @LastModifiedBy
  @Column(name = "last_modified_by", nullable = false)
  private String lastModifiedBy;

  @LastModifiedDate
  @Column(name = "last_modified_at", nullable = false)
  private Instant lastModifiedAt;

  @Version
  @Column(name = "version", nullable = false)
  private Long version;
}

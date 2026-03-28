package br.com.medflow.entities.seguranca;

import static br.com.medflow.entities.base.DomainValidation.required;
import static br.com.medflow.entities.base.DomainValidation.requiredText;

import java.time.Instant;

import br.com.medflow.entities.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    name = "token_revogado",
    indexes = {
      @Index(name = "ux_token_revogado_jti", columnList = "jti", unique = true),
      @Index(name = "idx_token_revogado_revogado_em", columnList = "revogado_em")
    })
public class TokenRevogado extends BaseEntity {

  @NotNull(message = "JTI do token é obrigatório")
  @Size(max = 255, message = "JTI do token deve ter no máximo 255 caracteres")
  @Column(name = "jti", nullable = false, length = 255, updatable = false)
  private String jti;

  @NotNull(message = "Data de revogação é obrigatória")
  @Column(name = "revogado_em", nullable = false, updatable = false)
  private Instant revogadoEm;

  public TokenRevogado(String jti, Instant revogadoEm) {
    this.jti = requiredText(jti, "JTI do token é obrigatório");
    this.revogadoEm = required(revogadoEm, "Data de revogação é obrigatória");
  }
}

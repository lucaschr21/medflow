package br.com.medflow.entities.agenda;

import static br.com.medflow.entities.base.DomainValidation.requireStrictlyBefore;
import static br.com.medflow.entities.base.DomainValidation.required;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
public class BloqueioAgenda {

  @NotNull(message = "Início do bloqueio é obrigatório")
  private LocalDateTime inicio;

  @NotNull(message = "Fim do bloqueio é obrigatório")
  private LocalDateTime fim;

  @NotNull(message = "Escopo do bloqueio é obrigatório")
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Enumerated(EnumType.STRING)
  private EscopoBloqueioAgenda escopo;

  public BloqueioAgenda(LocalDateTime inicio, LocalDateTime fim, EscopoBloqueioAgenda escopo) {
    this.inicio = required(inicio, "Início do bloqueio é obrigatório");
    this.fim = required(fim, "Fim do bloqueio é obrigatório");
    this.escopo = required(escopo, "Escopo do bloqueio é obrigatório");
    requireStrictlyBefore(this.inicio, this.fim, "Início deve ser anterior ao fim do bloqueio");
  }

  @AssertTrue(message = "Início deve ser anterior ao fim do bloqueio")
  private boolean isIntervaloValido() {
    if (inicio == null || fim == null) {
      return true;
    }
    return inicio.isBefore(fim);
  }
}

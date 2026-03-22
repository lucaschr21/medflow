package br.com.medflow.entities.agenda;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BloqueioAgenda {

    @NotNull(message = "Início do bloqueio é obrigatório")
    private LocalDateTime inicio;

    @NotNull(message = "Fim do bloqueio é obrigatório")
    private LocalDateTime fim;

    public BloqueioAgenda(LocalDateTime inicio, LocalDateTime fim) {
        this.inicio = Objects.requireNonNull(inicio, "Início do bloqueio é obrigatório");
        this.fim = Objects.requireNonNull(fim, "Fim do bloqueio é obrigatório");
        if (!this.inicio.isBefore(this.fim)) {
            throw new IllegalArgumentException("Início deve ser anterior ao fim do bloqueio");
        }
    }

    @AssertTrue(message = "Início deve ser anterior ao fim do bloqueio")
    private boolean isIntervaloValido() {
        if (inicio == null || fim == null) {
            return true;
        }
        return inicio.isBefore(fim);
    }
}

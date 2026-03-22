package br.com.medflow.entities.pessoas;

import java.util.Objects;

import br.com.medflow.entities.estrutura.Organizacao;
import br.com.medflow.entities.estrutura.Unidade;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "administrador")
public class Administrador extends Utilizador {

    @Size(max = 80, message = "Cargo deve ter no máximo 80 caracteres")
    @Column(length = 80)
    private String cargo;

    @NotNull(message = "Organização é obrigatória")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizacao_id", nullable = false)
    private Organizacao organizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_id")
    private Unidade unidade;

    public void definirCargo(String cargo) {
        this.cargo = cargo;
    }

    public void definirOrganizacao(Organizacao organizacao) {
        this.organizacao = Objects.requireNonNull(organizacao, "Organização é obrigatória");
    }

    public void definirUnidade(Unidade unidade) {
        this.unidade = unidade;
    }
}

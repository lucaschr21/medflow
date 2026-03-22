package br.com.medflow.entities.pessoas;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import br.com.medflow.entities.agenda.Agenda;
import br.com.medflow.entities.atendimento.Consulta;
import br.com.medflow.entities.base.BaseEntity;
import br.com.medflow.entities.estrutura.Consultorio;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "medico")
public class Medico extends BaseEntity {

    @NotNull(message = "Utilizador é obrigatório")
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "utilizador_id", nullable = false, unique = true)
    private Utilizador utilizador;

    @Size(max = 120, message = "Especialidade deve ter no máximo 120 caracteres")
    @Column(length = 120)
    private String especialidade;

    @NotBlank(message = "Número da ordem é obrigatório")
    @Size(max = 40, message = "Número da ordem deve ter no máximo 40 caracteres")
    @Column(name = "numero_ordem", nullable = false, unique = true, length = 40)
    private String numeroOrdem;

    @OneToOne(mappedBy = "medico", fetch = FetchType.LAZY)
    private Agenda agenda;

    @OneToMany(mappedBy = "medico", fetch = FetchType.LAZY)
    private Set<Consulta> consultas = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "medicos", fetch = FetchType.LAZY)
    private Set<Consultorio> consultorios = new LinkedHashSet<>();

    public void definirEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public void definirUtilizador(Utilizador utilizador) {
        this.utilizador = Objects.requireNonNull(utilizador, "Utilizador é obrigatório");
    }

    public void definirNumeroOrdem(String numeroOrdem) {
        this.numeroOrdem = Objects.requireNonNull(numeroOrdem, "Número da ordem é obrigatório");
    }

    public void adicionarConsultorio(Consultorio consultorio) {
        Consultorio item = Objects.requireNonNull(consultorio, "Consultório é obrigatório");
        this.consultorios.add(item);
        item.getMedicos().add(this);
    }

    public void removerConsultorio(Consultorio consultorio) {
        Consultorio item = Objects.requireNonNull(consultorio, "Consultório é obrigatório");
        if (this.consultorios.remove(item)) {
            item.getMedicos().remove(this);
        }
    }

    public void definirAgenda(Agenda agenda) {
        this.agenda = Objects.requireNonNull(agenda, "Agenda é obrigatória");
    }
}

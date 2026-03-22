package br.com.medflow.entities.pessoas;

import br.com.medflow.entities.agenda.Agenda;
import br.com.medflow.entities.atendimento.Consulta;
import br.com.medflow.entities.base.BaseEntity;
import br.com.medflow.entities.estrutura.Consultorio;
import br.com.medflow.entities.estrutura.ConsultorioMedico;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
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

  @OneToMany(mappedBy = "medico", fetch = FetchType.LAZY)
  private Set<ConsultorioMedico> vinculacoesConsultorios = new LinkedHashSet<>();

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
    Objects.requireNonNull(consultorio, "Consultório é obrigatório");
    consultorio.adicionarMedico(this);
  }

  public void removerConsultorio(Consultorio consultorio) {
    Objects.requireNonNull(consultorio, "Consultório é obrigatório");
    consultorio.removerMedico(this);
  }

  public void definirAgenda(Agenda agenda) {
    this.agenda = Objects.requireNonNull(agenda, "Agenda é obrigatória");
  }

  public Set<Consultorio> getConsultorios() {
    return vinculacoesConsultorios.stream()
        .map(ConsultorioMedico::getConsultorio)
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  public void adicionarVinculoConsultorio(ConsultorioMedico vinculo) {
    this.vinculacoesConsultorios.add(vinculo);
  }

  public void removerVinculoConsultorio(ConsultorioMedico vinculo) {
    this.vinculacoesConsultorios.remove(vinculo);
  }
}

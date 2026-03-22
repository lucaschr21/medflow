package br.com.medflow.entities.estrutura;

import br.com.medflow.entities.atendimento.Consulta;
import br.com.medflow.entities.base.BaseEntity;
import br.com.medflow.entities.pessoas.Medico;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "consultorio")
public class Consultorio extends BaseEntity {

  @NotBlank(message = "Nome do consultório é obrigatório")
  @Size(max = 180, message = "Nome do consultório deve ter no máximo 180 caracteres")
  @Column(name = "nome_consultorio", nullable = false, length = 180)
  private String nomeConsultorio;

  @Size(max = 40, message = "Número da sala deve ter no máximo 40 caracteres")
  @Column(name = "numero_sala", length = 40)
  private String numeroSala;

  @Size(max = 32, message = "Telefone de contato deve ter no máximo 32 caracteres")
  @Column(name = "telefone_contato", length = 32)
  private String telefoneContato;

  @NotNull(message = "Unidade é obrigatória")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "unidade_id", nullable = false)
  private Unidade unidade;

  @OneToMany(
      mappedBy = "consultorio",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private Set<ConsultorioMedico> vinculacoesMedicos = new LinkedHashSet<>();

  @OneToMany(mappedBy = "consultorio", fetch = FetchType.LAZY)
  private Set<Consulta> consultas = new LinkedHashSet<>();

  public void definirNomeConsultorio(String nomeConsultorio) {
    this.nomeConsultorio =
        Objects.requireNonNull(nomeConsultorio, "Nome do consultório é obrigatório");
  }

  public void definirNumeroSala(String numeroSala) {
    this.numeroSala = numeroSala;
  }

  public void definirTelefoneContato(String telefoneContato) {
    this.telefoneContato = telefoneContato;
  }

  void setUnidade(Unidade unidade) {
    this.unidade = Objects.requireNonNull(unidade, "Unidade é obrigatória");
  }

  public void adicionarMedico(Medico medico) {
    Medico profissional = Objects.requireNonNull(medico, "Médico é obrigatório");
    boolean existeVinculo =
        this.vinculacoesMedicos.stream()
            .anyMatch(vinculo -> Objects.equals(vinculo.getMedico(), profissional));
    if (existeVinculo) {
      return;
    }
    ConsultorioMedico vinculo = new ConsultorioMedico(this, profissional);
    this.vinculacoesMedicos.add(vinculo);
    profissional.adicionarVinculoConsultorio(vinculo);
  }

  public void removerMedico(Medico medico) {
    Medico profissional = Objects.requireNonNull(medico, "Médico é obrigatório");
    this.vinculacoesMedicos.removeIf(
        vinculo -> {
          boolean mesmoMedico = Objects.equals(vinculo.getMedico(), profissional);
          if (mesmoMedico) {
            profissional.removerVinculoConsultorio(vinculo);
          }
          return mesmoMedico;
        });
  }

  public boolean possuiMedico(Medico medico) {
    Medico profissional = Objects.requireNonNull(medico, "Médico é obrigatório");
    return this.vinculacoesMedicos.stream()
        .anyMatch(vinculo -> Objects.equals(vinculo.getMedico(), profissional));
  }

  public Set<Medico> getMedicos() {
    return vinculacoesMedicos.stream()
        .map(ConsultorioMedico::getMedico)
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }
}

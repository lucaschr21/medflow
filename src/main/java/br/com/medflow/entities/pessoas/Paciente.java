package br.com.medflow.entities.pessoas;

import br.com.medflow.entities.atendimento.Consulta;
import br.com.medflow.entities.atendimento.ProcessoClinico;
import br.com.medflow.entities.base.BaseEntity;
import br.com.medflow.entities.comum.Endereco;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "paciente")
public class Paciente extends BaseEntity {

  @NotNull(message = "Utilizador é obrigatório")
  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "utilizador_id", nullable = false, unique = true)
  private Utilizador utilizador;

  @PastOrPresent(message = "Data de nascimento deve ser hoje ou no passado")
  @Column(name = "data_nascimento")
  private LocalDate dataNascimento;

  @Size(max = 20, message = "NIF deve ter no máximo 20 caracteres")
  @Column(length = 20)
  private String nif;

  @NotNull(message = "Endereço é obrigatório")
  @Valid
  @Embedded
  private Endereco endereco;

  @Size(max = 80, message = "Número de beneficiário deve ter no máximo 80 caracteres")
  @Column(name = "numero_beneficiario", length = 80)
  private String numeroBeneficiario;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "processo_clinico_id", unique = true)
  private ProcessoClinico processoClinico;

  @OneToMany(mappedBy = "paciente", fetch = FetchType.LAZY)
  private Set<Consulta> consultas = new LinkedHashSet<>();

  public void definirDataNascimento(LocalDate dataNascimento) {
    this.dataNascimento = dataNascimento;
  }

  public void definirUtilizador(Utilizador utilizador) {
    this.utilizador = Objects.requireNonNull(utilizador, "Utilizador é obrigatório");
  }

  public void definirNif(String nif) {
    this.nif = nif;
  }

  public void definirEndereco(Endereco endereco) {
    this.endereco = Objects.requireNonNull(endereco, "Endereço é obrigatório");
  }

  public void definirNumeroBeneficiario(String numeroBeneficiario) {
    this.numeroBeneficiario = numeroBeneficiario;
  }

  public void definirProcessoClinico(ProcessoClinico processoClinico) {
    this.processoClinico = processoClinico;
  }
}

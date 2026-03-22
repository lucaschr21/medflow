package br.com.medflow.entities.atendimento;

import br.com.medflow.entities.base.BaseEntity;
import br.com.medflow.entities.pessoas.Paciente;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "processo_clinico")
public class ProcessoClinico extends BaseEntity {

  @NotNull(message = "Data de criação é obrigatória")
  @PastOrPresent(message = "Data de criação deve ser hoje ou no passado")
  @Column(name = "data_criacao", nullable = false)
  private LocalDate dataCriacao = LocalDate.now();

  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_sangue")
  private TipoSanguineo tipoSangue;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(
      name = "processo_clinico_alergia",
      joinColumns = @JoinColumn(name = "processo_clinico_id"),
      uniqueConstraints =
          @UniqueConstraint(
              name = "uk_processo_clinico_alergia",
              columnNames = {"processo_clinico_id", "alergia"}))
  @Column(name = "alergia", nullable = false, length = 120)
  private Set<
          @NotBlank(message = "Alergia não pode ser vazia")
          @Size(max = 120, message = "Alergia deve ter no máximo 120 caracteres") String>
      alergias = new LinkedHashSet<>();

  @OneToOne(mappedBy = "processoClinico", fetch = FetchType.LAZY)
  private Paciente paciente;

  @OneToMany(mappedBy = "processoClinico", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<@Valid RegistroAtendimento> registros = new LinkedHashSet<>();

  public void definirDataCriacao(LocalDate dataCriacao) {
    this.dataCriacao = Objects.requireNonNull(dataCriacao, "Data de criação é obrigatória");
  }

  public void definirTipoSangue(TipoSanguineo tipoSangue) {
    this.tipoSangue = tipoSangue;
  }

  public void adicionarAlergia(String alergia) {
    String valor = Objects.requireNonNull(alergia, "Alergia é obrigatória").strip();
    if (valor.isEmpty()) {
      throw new IllegalArgumentException("Alergia não pode ser vazia");
    }
    this.alergias.add(valor);
  }

  public void removerAlergia(String alergia) {
    String valor = Objects.requireNonNull(alergia, "Alergia é obrigatória").strip();
    if (valor.isEmpty()) {
      return;
    }
    this.alergias.remove(valor);
  }

  public void adicionarRegistro(RegistroAtendimento registro) {
    RegistroAtendimento novoRegistro =
        Objects.requireNonNull(registro, "Registro de atendimento é obrigatório");
    novoRegistro.setProcessoClinico(this);
    this.registros.add(novoRegistro);
  }

  public void removerRegistro(RegistroAtendimento registro) {
    RegistroAtendimento registroExistente =
        Objects.requireNonNull(registro, "Registro de atendimento é obrigatório");
    this.registros.remove(registroExistente);
  }
}

package br.com.medflow.entities.estrutura;

import br.com.medflow.entities.base.BaseEntity;
import br.com.medflow.entities.comum.Endereco;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CheckConstraint;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(
    name = "unidade",
    indexes = @Index(name = "idx_unidade_organizacao", columnList = "organizacao_id"),
    check = {
      @CheckConstraint(constraint = "latitude IS NULL OR latitude BETWEEN -90 AND 90"),
      @CheckConstraint(constraint = "longitude IS NULL OR longitude BETWEEN -180 AND 180")
    })
public class Unidade extends BaseEntity {

  @NotBlank(message = "Nome da unidade é obrigatório")
  @Size(max = 180, message = "Nome da unidade deve ter no máximo 180 caracteres")
  @Column(name = "nome", nullable = false, length = 180)
  private String nome;

  @NotNull(message = "Endereço é obrigatório")
  @Valid
  @Embedded
  private Endereco endereco;

  @Column(name = "latitude", precision = 10, scale = 7)
  private BigDecimal latitude;

  @Column(name = "longitude", precision = 10, scale = 7)
  private BigDecimal longitude;

  @NotNull(message = "Organização é obrigatória")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "organizacao_id", nullable = false)
  private Organizacao organizacao;

  @OneToMany(
      mappedBy = "unidade",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private Set<@Valid Consultorio> consultorios = new LinkedHashSet<>();

  public void definirNome(String nome) {
    String valor = Objects.requireNonNull(nome, "Nome da unidade é obrigatório").strip();
    if (valor.isEmpty()) {
      throw new IllegalArgumentException("Nome da unidade é obrigatório");
    }
    this.nome = valor;
  }

  public void definirEndereco(Endereco endereco) {
    this.endereco = Objects.requireNonNull(endereco, "Endereço é obrigatório");
  }

  public void definirLatitude(BigDecimal latitude) {
    if (latitude != null
        && (latitude.compareTo(BigDecimal.valueOf(-90)) < 0
            || latitude.compareTo(BigDecimal.valueOf(90)) > 0)) {
      throw new IllegalArgumentException("Latitude deve estar entre -90 e 90");
    }
    this.latitude = latitude;
  }

  public void definirLongitude(BigDecimal longitude) {
    if (longitude != null
        && (longitude.compareTo(BigDecimal.valueOf(-180)) < 0
            || longitude.compareTo(BigDecimal.valueOf(180)) > 0)) {
      throw new IllegalArgumentException("Longitude deve estar entre -180 e 180");
    }
    this.longitude = longitude;
  }

  void setOrganizacao(Organizacao organizacao) {
    this.organizacao = Objects.requireNonNull(organizacao, "Organização é obrigatória");
  }

  public void adicionarConsultorio(Consultorio consultorio) {
    Consultorio item = Objects.requireNonNull(consultorio, "Consultório é obrigatório");
    item.setUnidade(this);
    this.consultorios.add(item);
  }

  public void removerConsultorio(Consultorio consultorio) {
    this.consultorios.remove(Objects.requireNonNull(consultorio, "Consultório é obrigatório"));
  }
}

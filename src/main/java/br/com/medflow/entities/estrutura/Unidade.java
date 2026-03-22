package br.com.medflow.entities.estrutura;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import br.com.medflow.entities.base.BaseEntity;
import br.com.medflow.entities.comum.Endereco;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "unidade")
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

    @OneToMany(mappedBy = "unidade", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<@Valid Consultorio> consultorios = new LinkedHashSet<>();

    public void definirNome(String nome) {
        this.nome = Objects.requireNonNull(nome, "Nome da unidade é obrigatório");
    }

    public void definirEndereco(Endereco endereco) {
        this.endereco = Objects.requireNonNull(endereco, "Endereço é obrigatório");
    }

    public void definirLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public void definirLongitude(BigDecimal longitude) {
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
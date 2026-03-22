package br.com.medflow.entities.financeiro;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import br.com.medflow.entities.atendimento.Consulta;
import br.com.medflow.entities.base.BaseEntity;
import br.com.medflow.entities.estrutura.Organizacao;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "convenio")
public class Convenio extends BaseEntity {

    @NotBlank(message = "Nome fantasia é obrigatório")
    @Size(max = 180, message = "Nome fantasia deve ter no máximo 180 caracteres")
    @Column(name = "nome_fantasia", nullable = false, length = 180)
    private String nomeFantasia;

    @NotNull(message = "Tipo de convênio é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private TipoConvenio tipo;

    @Size(max = 80, message = "Registro da entidade reguladora deve ter no máximo 80 caracteres")
    @Column(name = "registro_entidade_reguladora", length = 80)
    private String registroEntidadeReguladora;

    @NotNull(message = "Status de atividade é obrigatório")
    @Column(nullable = false)
    private Boolean ativo = Boolean.TRUE;

    @NotNull(message = "Organização é obrigatória")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizacao_id", nullable = false)
    private Organizacao organizacao;

    @OneToMany(mappedBy = "convenio", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<@Valid ProcedimentoPreco> procedimentosPrecos = new LinkedHashSet<>();

    @OneToMany(mappedBy = "convenio", fetch = FetchType.LAZY)
    private Set<Consulta> consultas = new LinkedHashSet<>();

    public void definirNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = Objects.requireNonNull(nomeFantasia, "Nome fantasia é obrigatório");
    }

    public void definirTipo(TipoConvenio tipo) {
        this.tipo = Objects.requireNonNull(tipo, "Tipo de convênio é obrigatório");
    }

    public void definirRegistroEntidadeReguladora(String registroEntidadeReguladora) {
        this.registroEntidadeReguladora = registroEntidadeReguladora;
    }

    public void definirOrganizacao(Organizacao organizacao) {
        this.organizacao = Objects.requireNonNull(organizacao, "Organização é obrigatória");
    }

    public void ativar() {
        this.ativo = Boolean.TRUE;
    }

    public void desativar() {
        this.ativo = Boolean.FALSE;
    }

    public void adicionarProcedimentoPreco(ProcedimentoPreco procedimentoPreco) {
        ProcedimentoPreco item = Objects.requireNonNull(procedimentoPreco, "Procedimento é obrigatório");
        item.setConvenio(this);
        this.procedimentosPrecos.add(item);
    }

    public void removerProcedimentoPreco(ProcedimentoPreco procedimentoPreco) {
        this.procedimentosPrecos.remove(Objects.requireNonNull(procedimentoPreco, "Procedimento é obrigatório"));
    }
}

package br.com.medflow.entities.estrutura;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import br.com.medflow.entities.base.BaseEntity;
import br.com.medflow.entities.financeiro.Convenio;
import br.com.medflow.entities.pessoas.Administrador;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
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
@Table(name = "organizacao")
public class Organizacao extends BaseEntity {

    @NotBlank(message = "Nome fantasia é obrigatório")
    @Size(max = 180, message = "Nome fantasia deve ter no máximo 180 caracteres")
    @Column(name = "nome_fantasia", nullable = false, length = 180)
    private String nomeFantasia;

    @NotBlank(message = "CNPJ é obrigatório")
    @Size(max = 20, message = "CNPJ deve ter no máximo 20 caracteres")
    @Column(nullable = false, unique = true, length = 20)
    private String cnpj;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "logotipo_imagem")
    private byte[] logotipoImagem;

    @NotNull(message = "Configurações globais são obrigatórias")
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "configuracoes_globais", columnDefinition = "jsonb")
    private Map<String, Object> configuracoesGlobais = new LinkedHashMap<>();

    @OneToMany(mappedBy = "organizacao", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<@Valid Unidade> unidades = new LinkedHashSet<>();

    @OneToMany(mappedBy = "organizacao", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<@Valid Convenio> convenios = new LinkedHashSet<>();

    @OneToMany(mappedBy = "organizacao", fetch = FetchType.LAZY)
    private Set<Administrador> administradores = new LinkedHashSet<>();

    public void definirNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = Objects.requireNonNull(nomeFantasia, "Nome fantasia é obrigatório");
    }

    public void definirCnpj(String cnpj) {
        this.cnpj = Objects.requireNonNull(cnpj, "CNPJ é obrigatório");
    }

    public void definirLogotipoImagem(byte[] logotipoImagem) {
        this.logotipoImagem = logotipoImagem == null
                ? null
                : Arrays.copyOf(logotipoImagem, logotipoImagem.length);
    }

    public byte[] getLogotipoImagem() {
        return logotipoImagem == null
                ? null
                : Arrays.copyOf(logotipoImagem, logotipoImagem.length);
    }

    public void atualizarConfiguracoesGlobais(Map<String, Object> configuracoesGlobais) {
        this.configuracoesGlobais.clear();
        this.configuracoesGlobais
                .putAll(Objects.requireNonNull(configuracoesGlobais, "Configurações globais são obrigatórias"));
    }

    public void adicionarUnidade(Unidade unidade) {
        Unidade item = Objects.requireNonNull(unidade, "Unidade é obrigatória");
        item.setOrganizacao(this);
        this.unidades.add(item);
    }

    public void removerUnidade(Unidade unidade) {
        this.unidades.remove(Objects.requireNonNull(unidade, "Unidade é obrigatória"));
    }

    public void adicionarConvenio(Convenio convenio) {
        Convenio item = Objects.requireNonNull(convenio, "Convênio é obrigatório");
        item.definirOrganizacao(this);
        this.convenios.add(item);
    }

    public void removerConvenio(Convenio convenio) {
        this.convenios.remove(Objects.requireNonNull(convenio, "Convênio é obrigatório"));
    }

    public void adicionarAdministrador(Administrador administrador) {
        Administrador item = Objects.requireNonNull(administrador, "Administrador é obrigatório");
        item.definirOrganizacao(this);
        this.administradores.add(item);
    }

    public void removerAdministrador(Administrador administrador) {
        this.administradores.remove(Objects.requireNonNull(administrador, "Administrador é obrigatório"));
    }
}

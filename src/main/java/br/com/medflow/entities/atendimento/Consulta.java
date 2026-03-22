package br.com.medflow.entities.atendimento;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import br.com.medflow.entities.base.BaseEntity;
import br.com.medflow.entities.estrutura.Consultorio;
import br.com.medflow.entities.financeiro.Convenio;
import br.com.medflow.entities.financeiro.Pagamento;
import br.com.medflow.entities.pessoas.Medico;
import br.com.medflow.entities.pessoas.Paciente;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "consulta")
public class Consulta extends BaseEntity {

    @NotNull(message = "Data e hora de marcação são obrigatórios")
    @Column(name = "data_hora_marcacao", nullable = false)
    private LocalDateTime dataHoraMarcacao;

    @NotNull(message = "Estado da consulta é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private EstadoConsulta estado = EstadoConsulta.AGENDADA;

    @Size(max = 80, message = "Tipo de consulta deve ter no máximo 80 caracteres")
    @Column(name = "tipo_consulta", length = 80)
    private String tipoConsulta;

    @NotNull(message = "Paciente é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @NotNull(message = "Médico é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @NotNull(message = "Consultório é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "consultorio_id", nullable = false)
    private Consultorio consultorio;

    @NotNull(message = "Convênio é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "convenio_id", nullable = false)
    private Convenio convenio;

    @OneToMany(mappedBy = "consulta", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<@Valid DocumentoMedico> documentosMedicos = new LinkedHashSet<>();

    @OneToOne(mappedBy = "consulta", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private RegistroAtendimento registroAtendimento;

    @OneToOne(mappedBy = "consulta", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Pagamento pagamento;

    public void definirDataHoraMarcacao(LocalDateTime dataHoraMarcacao) {
        this.dataHoraMarcacao = Objects.requireNonNull(dataHoraMarcacao, "Data e hora da marcação são obrigatórios");
    }

    public void definirEstado(EstadoConsulta estado) {
        this.estado = Objects.requireNonNull(estado, "Estado da consulta é obrigatório");
    }

    public void definirTipoConsulta(String tipoConsulta) {
        this.tipoConsulta = tipoConsulta;
    }

    public void definirPaciente(Paciente paciente) {
        this.paciente = Objects.requireNonNull(paciente, "Paciente é obrigatório");
    }

    public void definirMedico(Medico medico) {
        this.medico = Objects.requireNonNull(medico, "Médico é obrigatório");
    }

    public void definirConsultorio(Consultorio consultorio) {
        this.consultorio = Objects.requireNonNull(consultorio, "Consultório é obrigatório");
    }

    public void definirConvenio(Convenio convenio) {
        this.convenio = Objects.requireNonNull(convenio, "Convênio é obrigatório");
    }

    public void adicionarDocumentoMedico(DocumentoMedico documentoMedico) {
        DocumentoMedico documento = Objects.requireNonNull(documentoMedico, "Documento médico é obrigatório");
        documento.setConsulta(this);
        this.documentosMedicos.add(documento);
    }

    public void removerDocumentoMedico(DocumentoMedico documentoMedico) {
        DocumentoMedico documento = Objects.requireNonNull(documentoMedico, "Documento médico é obrigatório");
        this.documentosMedicos.remove(documento);
    }

    public void definirRegistroAtendimento(RegistroAtendimento registroAtendimento) {
        this.registroAtendimento = Objects.requireNonNull(registroAtendimento, "Registro de atendimento é obrigatório");
        this.registroAtendimento.setConsulta(this);
    }

    public void removerRegistroAtendimento() {
        this.registroAtendimento = null;
    }

    public void definirPagamento(Pagamento pagamento) {
        this.pagamento = Objects.requireNonNull(pagamento, "Pagamento é obrigatório");
        this.pagamento.setConsulta(this);
    }

    public void removerPagamento() {
        this.pagamento = null;
    }
}

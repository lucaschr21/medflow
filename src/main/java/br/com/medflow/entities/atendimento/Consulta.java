package br.com.medflow.entities.atendimento;

import static br.com.medflow.entities.base.DomainValidation.optionalText;
import static br.com.medflow.entities.base.DomainValidation.required;
import static br.com.medflow.entities.base.DomainValidation.requiredText;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import br.com.medflow.entities.base.BaseEntity;
import br.com.medflow.entities.estrutura.Consultorio;
import br.com.medflow.entities.estrutura.ConsultorioMedico;
import br.com.medflow.entities.financeiro.Convenio;
import br.com.medflow.entities.financeiro.Pagamento;
import br.com.medflow.entities.pessoas.Medico;
import br.com.medflow.entities.pessoas.Paciente;
import br.com.medflow.entities.pessoas.Utilizador;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
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
@Table(
    name = "consulta",
    indexes = {
      @Index(
          name = "ux_consulta_medico_data_hora_ativa",
          columnList = "medico_id, data_hora_marcacao",
          unique = true,
          options = "where estado <> 'CANCELADA'"),
      @Index(
          name = "ux_consulta_consultorio_data_hora_ativa",
          columnList = "consultorio_id, data_hora_marcacao",
          unique = true,
          options = "where estado <> 'CANCELADA'"),
      @Index(name = "idx_consulta_paciente", columnList = "paciente_id"),
      @Index(name = "idx_consulta_medico", columnList = "medico_id"),
      @Index(name = "idx_consulta_consultorio", columnList = "consultorio_id"),
      @Index(name = "idx_consulta_convenio", columnList = "convenio_id"),
      @Index(name = "idx_consulta_data_hora", columnList = "data_hora_marcacao")
    })
public class Consulta extends BaseEntity {

  @NotNull(message = "Data e hora de marcação são obrigatórios")
  @Column(name = "data_hora_marcacao", nullable = false)
  private LocalDateTime dataHoraMarcacao;

  @NotNull(message = "Estado da consulta é obrigatório")
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EstadoConsulta estado = EstadoConsulta.AGENDADA;

  @Size(max = 80, message = "Tipo de consulta deve ter no máximo 80 caracteres")
  @Column(name = "tipo_consulta", length = 80)
  private String tipoConsulta;

  @Size(max = 500, message = "Motivo de cancelamento deve ter no máximo 500 caracteres")
  @Column(name = "motivo_cancelamento", length = 500)
  private String motivoCancelamento;

  @Column(name = "cancelado_em")
  private LocalDateTime canceladoEm;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cancelado_por_utilizador_id")
  private Utilizador canceladoPor;

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

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumns(
      value = {
        @JoinColumn(
            name = "consultorio_id",
            referencedColumnName = "consultorio_id",
            nullable = false,
            insertable = false,
            updatable = false),
        @JoinColumn(
            name = "medico_id",
            referencedColumnName = "medico_id",
            nullable = false,
            insertable = false,
            updatable = false)
      },
      foreignKey = @ForeignKey(name = "fk_consulta_consultorio_medico"))
  private ConsultorioMedico vinculacaoConsultorioMedico;

  @OneToMany(mappedBy = "consulta", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<@Valid DocumentoMedico> documentosMedicos = new LinkedHashSet<>();

  @OneToOne(
      mappedBy = "consulta",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private RegistroAtendimento registroAtendimento;

  @OneToOne(
      mappedBy = "consulta",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private Pagamento pagamento;

  public void definirDataHoraMarcacao(LocalDateTime dataHoraMarcacao) {
    this.dataHoraMarcacao = required(dataHoraMarcacao, "Data e hora da marcação são obrigatórios");
  }

  public void definirEstado(EstadoConsulta estado) {
    this.estado = required(estado, "Estado da consulta é obrigatório");
    if (this.estado != EstadoConsulta.CANCELADA) {
      this.motivoCancelamento = null;
      this.canceladoEm = null;
      this.canceladoPor = null;
    }
  }

  public void definirTipoConsulta(String tipoConsulta) {
    this.tipoConsulta = optionalText(tipoConsulta);
  }

  public void definirPaciente(Paciente paciente) {
    this.paciente = required(paciente, "Paciente é obrigatório");
  }

  public void definirMedico(Medico medico) {
    Medico valor = required(medico, "Médico é obrigatório");
    validarCoerenciaMedicoConsultorio(valor, this.consultorio);
    this.medico = valor;
  }

  public void definirConsultorio(Consultorio consultorio) {
    Consultorio valor = required(consultorio, "Consultório é obrigatório");
    validarCoerenciaMedicoConsultorio(this.medico, valor);
    this.consultorio = valor;
  }

  public void definirConvenio(Convenio convenio) {
    this.convenio = required(convenio, "Convênio é obrigatório");
  }

  public void adicionarDocumentoMedico(DocumentoMedico documentoMedico) {
    DocumentoMedico documento = required(documentoMedico, "Documento médico é obrigatório");
    documento.setConsulta(this);
    this.documentosMedicos.add(documento);
  }

  public void removerDocumentoMedico(DocumentoMedico documentoMedico) {
    DocumentoMedico documento = required(documentoMedico, "Documento médico é obrigatório");
    this.documentosMedicos.remove(documento);
  }

  public void definirRegistroAtendimento(RegistroAtendimento registroAtendimento) {
    this.registroAtendimento =
        required(registroAtendimento, "Registro de atendimento é obrigatório");
    this.registroAtendimento.setConsulta(this);
  }

  public void removerRegistroAtendimento() {
    this.registroAtendimento = null;
  }

  public void definirPagamento(Pagamento pagamento) {
    this.pagamento = required(pagamento, "Pagamento é obrigatório");
    this.pagamento.setConsulta(this);
  }

  public void removerPagamento() {
    this.pagamento = null;
  }

  public void cancelar(String motivo, Utilizador canceladoPor) {
    String motivoObrigatorio = requiredText(motivo, "Motivo de cancelamento é obrigatório");
    this.estado = EstadoConsulta.CANCELADA;
    this.motivoCancelamento = motivoObrigatorio;
    this.canceladoPor =
        required(canceladoPor, "Utilizador responsável pelo cancelamento é obrigatório");
    this.canceladoEm = LocalDateTime.now();
  }

  private static void validarCoerenciaMedicoConsultorio(Medico medico, Consultorio consultorio) {
    if (medico == null || consultorio == null) {
      return;
    }
    if (!consultorio.possuiMedico(medico)) {
      throw new IllegalArgumentException(
          "O médico informado não está vinculado ao consultório informado");
    }
  }
}

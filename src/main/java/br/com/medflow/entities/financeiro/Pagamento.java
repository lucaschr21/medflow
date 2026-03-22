package br.com.medflow.entities.financeiro;

import static br.com.medflow.entities.base.DomainValidation.optionalText;
import static br.com.medflow.entities.base.DomainValidation.required;
import static br.com.medflow.entities.base.DomainValidation.requiredPositive;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import br.com.medflow.entities.atendimento.Consulta;
import br.com.medflow.entities.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "pagamento")
public class Pagamento extends BaseEntity {

  @NotNull(message = "Valor é obrigatório")
  @DecimalMin(value = "0.00", inclusive = false, message = "Valor deve ser maior que zero")
  @Column(nullable = false, precision = 14, scale = 2)
  private BigDecimal valor;

  @NotNull(message = "Data de vencimento é obrigatória")
  @Column(name = "data_vencimento", nullable = false)
  private LocalDate dataVencimento;

  @NotNull(message = "Estado do pagamento é obrigatório")
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EstadoPagamento estado = EstadoPagamento.PENDENTE;

  @Size(max = 80, message = "Método de pagamento deve ter no máximo 80 caracteres")
  @Column(name = "metodo_pagamento", length = 80)
  private String metodoPagamento;

  @NotNull(message = "Consulta é obrigatória")
  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "consulta_id", nullable = false, unique = true)
  private Consulta consulta;

  public void definirValor(BigDecimal valor) {
    this.valor = requiredPositive(valor, "Valor é obrigatório", "Valor deve ser maior que zero");
  }

  public void definirDataVencimento(LocalDate dataVencimento) {
    this.dataVencimento = required(dataVencimento, "Data de vencimento é obrigatória");
  }

  public void definirMetodoPagamento(String metodoPagamento) {
    this.metodoPagamento = optionalText(metodoPagamento);
  }

  public void setConsulta(Consulta consulta) {
    this.consulta = required(consulta, "Consulta é obrigatória");
  }

  public void marcarComoProcessado() {
    this.estado = EstadoPagamento.PROCESSADO;
  }

  public void marcarComoAtrasado() {
    this.estado = EstadoPagamento.ATRASADO;
  }

  public void cancelar() {
    this.estado = EstadoPagamento.CANCELADO;
  }

  public void estornar() {
    this.estado = EstadoPagamento.ESTORNADO;
  }
}

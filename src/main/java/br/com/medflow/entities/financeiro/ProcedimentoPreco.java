package br.com.medflow.entities.financeiro;

import static br.com.medflow.entities.base.DomainValidation.required;
import static br.com.medflow.entities.base.DomainValidation.requiredNonNegative;
import static br.com.medflow.entities.base.DomainValidation.requiredText;

import br.com.medflow.entities.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "procedimento_preco")
public class ProcedimentoPreco extends BaseEntity {

  @NotBlank(message = "Código do procedimento é obrigatório")
  @Size(max = 40, message = "Código do procedimento deve ter no máximo 40 caracteres")
  @Column(name = "codigo_procedimento", nullable = false, length = 40)
  private String codigoProcedimento;

  @NotBlank(message = "Descrição é obrigatória")
  @Size(max = 255, message = "Descrição deve ter no máximo 255 caracteres")
  @Column(nullable = false, length = 255)
  private String descricao;

  @NotNull(message = "Valor total é obrigatório")
  @DecimalMin(value = "0.00", inclusive = true, message = "Valor total não pode ser negativo")
  @Column(name = "valor_total", nullable = false, precision = 14, scale = 2)
  private BigDecimal valorTotal;

  @NotNull(message = "Valor do utente é obrigatório")
  @DecimalMin(value = "0.00", inclusive = true, message = "Valor do utente não pode ser negativo")
  @Column(name = "valor_utente", nullable = false, precision = 14, scale = 2)
  private BigDecimal valorUtente;

  @NotNull(message = "Valor da seguradora é obrigatório")
  @DecimalMin(
      value = "0.00",
      inclusive = true,
      message = "Valor da seguradora não pode ser negativo")
  @Column(name = "valor_seguradora", nullable = false, precision = 14, scale = 2)
  private BigDecimal valorSeguradora;

  @NotNull(message = "Convênio é obrigatório")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "convenio_id", nullable = false)
  private Convenio convenio;

  public void definirCodigoProcedimento(String codigoProcedimento) {
    this.codigoProcedimento =
        requiredText(codigoProcedimento, "Código do procedimento é obrigatório");
  }

  public void definirDescricao(String descricao) {
    this.descricao = requiredText(descricao, "Descrição é obrigatória");
  }

  public void definirValorTotal(BigDecimal valorTotal) {
    this.valorTotal =
        requiredNonNegative(valorTotal, "Valor total é obrigatório", "Valor não pode ser negativo");
  }

  public void definirValorUtente(BigDecimal valorUtente) {
    this.valorUtente =
        requiredNonNegative(
            valorUtente, "Valor do utente é obrigatório", "Valor não pode ser negativo");
  }

  public void definirValorSeguradora(BigDecimal valorSeguradora) {
    this.valorSeguradora =
        requiredNonNegative(
            valorSeguradora, "Valor da seguradora é obrigatório", "Valor não pode ser negativo");
  }

  void setConvenio(Convenio convenio) {
    this.convenio = required(convenio, "Convênio é obrigatório");
  }
}

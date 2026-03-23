package br.com.medflow.entities.comum;

import static br.com.medflow.entities.base.DomainValidation.optionalText;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class Endereco {

  @Size(max = 160, message = "Logradouro deve ter no máximo 160 caracteres")
  @Column(name = "logradouro", length = 160)
  private String logradouro;

  @Size(max = 20, message = "Número deve ter no máximo 20 caracteres")
  @Column(name = "numero", length = 20)
  private String numero;

  @Size(max = 120, message = "Bairro deve ter no máximo 120 caracteres")
  @Column(name = "bairro", length = 120)
  private String bairro;

  @Size(max = 120, message = "Cidade deve ter no máximo 120 caracteres")
  @Column(name = "cidade", length = 120)
  private String cidade;

  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Enumerated(EnumType.STRING)
  @Column(name = "uf")
  private UF uf;

  @Size(max = 16, message = "CEP deve ter no máximo 16 caracteres")
  @Column(name = "cep", length = 16)
  private String cep;

  @Size(max = 120, message = "Complemento deve ter no máximo 120 caracteres")
  @Column(name = "complemento", length = 120)
  private String complemento;

  public void definirLogradouro(String logradouro) {
    this.logradouro = optionalText(logradouro);
  }

  public void definirNumero(String numero) {
    this.numero = optionalText(numero);
  }

  public void definirBairro(String bairro) {
    this.bairro = optionalText(bairro);
  }

  public void definirCidade(String cidade) {
    this.cidade = optionalText(cidade);
  }

  public void definirUf(UF uf) {
    this.uf = uf;
  }

  public void definirCep(String cep) {
    this.cep = optionalText(cep);
  }

  public void definirComplemento(String complemento) {
    this.complemento = optionalText(complemento);
  }
}

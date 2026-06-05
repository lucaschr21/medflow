package br.com.medflow.entities;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import br.com.medflow.entities.enums.Uf;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa um endereço embutido.
 */
@Getter
@Setter
@Embeddable
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Endereco {

  @Column(name = "logradouro", nullable = false)
  private String logradouro;

  @Column(name = "numero", nullable = false)
  private String numero;

  @Column(name = "bairro", nullable = false)
  private String bairro;

  @Column(name = "cidade", nullable = false)
  private String cidade;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(name = "uf", nullable = false)
  private Uf uf;

  @Column(name = "cep", nullable = false, length = 8)
  private String cep;

  @Column(name = "complemento")
  private String complemento;
}

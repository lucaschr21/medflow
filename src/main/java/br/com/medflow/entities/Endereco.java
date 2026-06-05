package br.com.medflow.entities;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import br.com.medflow.entities.enums.Uf;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa um endereço embutido.
 */
@Getter
@Setter
@Embeddable
@NoArgsConstructor
public class Endereco {

  @NotBlank(message = "O logradouro é obrigatório.")
  @Size(max = 160, message = "O logradouro deve ter no máximo 160 caracteres.")
  @Column(name = "logradouro", nullable = false, length = 160)
  private String logradouro;

  @NotBlank(message = "O número do endereço é obrigatório.")
  @Size(max = 20, message = "O número do endereço deve ter no máximo 20 caracteres.")
  @Column(name = "numero", nullable = false, length = 20)
  private String numero;

  @NotBlank(message = "O bairro é obrigatório.")
  @Size(max = 80, message = "O bairro deve ter no máximo 80 caracteres.")
  @Column(name = "bairro", nullable = false, length = 80)
  private String bairro;

  @NotBlank(message = "A cidade é obrigatória.")
  @Size(max = 120, message = "A cidade deve ter no máximo 120 caracteres.")
  @Column(name = "cidade", nullable = false, length = 120)
  private String cidade;

  @NotNull(message = "A UF é obrigatória.")
  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(name = "uf", nullable = false)
  private Uf uf;

  @NotBlank(message = "O CEP é obrigatório.")
  @Pattern(regexp = "\\d{8}", message = "O CEP deve conter exatamente 8 dígitos.")
  @Size(min = 8, max = 8, message = "O CEP deve conter exatamente 8 dígitos.")
  @Column(name = "cep", nullable = false, length = 8)
  private String cep;

  @Size(max = 120, message = "O complemento deve ter no máximo 120 caracteres.")
  @Column(name = "complemento", length = 120)
  private String complemento;
}

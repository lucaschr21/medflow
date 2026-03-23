package br.com.medflow.entities.pessoas;

import static br.com.medflow.entities.base.DomainValidation.optionalText;
import static br.com.medflow.entities.base.DomainValidation.requiredText;

import br.com.medflow.entities.base.BaseEntity;
import jakarta.persistence.CheckConstraint;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Locale;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "utilizador", check = @CheckConstraint(constraint = "email = lower(email)"))
public class Utilizador extends BaseEntity {

  @NotBlank(message = "Nome é obrigatório")
  @Size(max = 160, message = "Nome deve ter no máximo 160 caracteres")
  @Column(nullable = false, length = 160)
  private String nome;

  @NotBlank(message = "Email é obrigatório")
  @Email(message = "Email inválido")
  @Size(max = 180, message = "Email deve ter no máximo 180 caracteres")
  @Column(nullable = false, unique = true, length = 180)
  private String email;

  @NotBlank(message = "Hash da senha é obrigatório")
  @Size(max = 255, message = "Hash da senha deve ter no máximo 255 caracteres")
  @Column(name = "password_hash", nullable = false, length = 255)
  private String passwordHash;

  @Size(max = 32, message = "Telefone deve ter no máximo 32 caracteres")
  @Column(length = 32)
  private String telefone;

  public void definirNome(String nome) {
    this.nome = requiredText(nome, "Nome é obrigatório");
  }

  public void definirEmail(String email) {
    String valor = requiredText(email, "Email é obrigatório");
    this.email = valor.toLowerCase(Locale.ROOT);
  }

  public void definirPasswordHash(String passwordHash) {
    this.passwordHash = requiredText(passwordHash, "Hash da senha é obrigatório");
  }

  public void definirTelefone(String telefone) {
    this.telefone = optionalText(telefone);
  }
}

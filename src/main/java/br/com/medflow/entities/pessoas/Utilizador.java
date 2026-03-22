package br.com.medflow.entities.pessoas;

import br.com.medflow.entities.base.BaseEntity;
import jakarta.persistence.CheckConstraint;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Locale;
import java.util.Objects;
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
    String valor = Objects.requireNonNull(nome, "Nome é obrigatório").trim();
    if (valor.isEmpty()) {
      throw new IllegalArgumentException("Nome é obrigatório");
    }
    this.nome = valor;
  }

  public void definirEmail(String email) {
    String valor = Objects.requireNonNull(email, "Email é obrigatório").trim();
    this.email = valor.toLowerCase(Locale.ROOT);
  }

  public void definirPasswordHash(String passwordHash) {
    String valor = Objects.requireNonNull(passwordHash, "Hash da senha é obrigatório").trim();
    if (valor.isEmpty()) {
      throw new IllegalArgumentException("Hash da senha é obrigatório");
    }
    this.passwordHash = valor;
  }

  public void definirTelefone(String telefone) {
    if (telefone == null) {
      this.telefone = null;
      return;
    }
    String valor = telefone.trim();
    this.telefone = valor.isEmpty() ? null : valor;
  }
}

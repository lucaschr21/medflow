package br.com.medflow.entities.pessoas;

import java.util.Locale;
import java.util.Objects;

import br.com.medflow.entities.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "utilizador")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_utilizador")
public abstract class Utilizador extends BaseEntity {

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
        this.nome = Objects.requireNonNull(nome, "Nome é obrigatório");
    }

    public void definirEmail(String email) {
        String valor = Objects.requireNonNull(email, "Email é obrigatório").trim();
        this.email = valor.toLowerCase(Locale.ROOT);
    }

    public void definirPasswordHash(String passwordHash) {
        this.passwordHash = Objects.requireNonNull(passwordHash, "Hash da senha é obrigatório");
    }

    public void definirTelefone(String telefone) {
        this.telefone = telefone;
    }
}

package br.com.medflow.repositories.pessoas;

import br.com.medflow.entities.pessoas.Utilizador;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilizadorRepository extends JpaRepository<Utilizador, UUID> {

  Optional<Utilizador> findByEmail(String email);

  boolean existsByEmail(String email);
}

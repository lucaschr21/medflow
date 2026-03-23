package br.com.medflow.repositories.pessoas;

import br.com.medflow.entities.pessoas.Medico;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicoRepository extends JpaRepository<Medico, UUID> {

  Optional<Medico> findByNumeroOrdem(String numeroOrdem);

  boolean existsByNumeroOrdem(String numeroOrdem);

  Optional<Medico> findByUtilizadorId(UUID utilizadorId);

  boolean existsByUtilizadorId(UUID utilizadorId);
}

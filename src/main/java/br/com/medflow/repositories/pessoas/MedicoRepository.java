package br.com.medflow.repositories.pessoas;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.medflow.entities.pessoas.Medico;

public interface MedicoRepository extends JpaRepository<Medico, UUID> {

  Optional<Medico> findByNumeroOrdem(String numeroOrdem);

  boolean existsByNumeroOrdem(String numeroOrdem);

  Optional<Medico> findByUtilizadorId(UUID utilizadorId);

  boolean existsByUtilizadorId(UUID utilizadorId);
}

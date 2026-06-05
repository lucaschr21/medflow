package br.com.medflow.repositories;

import java.util.Optional;
import java.util.UUID;

import br.com.medflow.core.persistence.CommonRepository;
import br.com.medflow.entities.Medico;

/**
 * Repository da entidade {@link Medico}.
 */
public interface MedicoRepository extends CommonRepository<Medico, UUID> {

  /**
   * Localiza o médico pelo identificador do usuário associado.
   *
   * @param usuarioId identificador do usuário
   * @return médico encontrado, quando existir
   */
  Optional<Medico> findByUsuarioId(UUID usuarioId);
}

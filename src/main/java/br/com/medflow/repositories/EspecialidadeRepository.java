package br.com.medflow.repositories;

import java.util.UUID;

import br.com.medflow.core.persistence.CommonRepository;
import br.com.medflow.entities.Especialidade;

/**
 * Repository da entidade {@link Especialidade}.
 */
public interface EspecialidadeRepository extends CommonRepository<Especialidade, UUID> {
}

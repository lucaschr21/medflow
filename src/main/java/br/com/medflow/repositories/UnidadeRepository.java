package br.com.medflow.repositories;

import java.util.UUID;

import br.com.medflow.core.persistence.CommonRepository;
import br.com.medflow.entities.Unidade;

/**
 * Repository da entidade {@link Unidade}.
 */
public interface UnidadeRepository extends CommonRepository<Unidade, UUID> {
}

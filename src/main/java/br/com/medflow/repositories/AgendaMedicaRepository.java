package br.com.medflow.repositories;

import java.util.UUID;

import br.com.medflow.core.persistence.CommonRepository;
import br.com.medflow.entities.AgendaMedica;

/**
 * Repository da entidade {@link AgendaMedica}.
 */
public interface AgendaMedicaRepository extends CommonRepository<AgendaMedica, UUID> {
}

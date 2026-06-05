package br.com.medflow.repositories;

import java.util.UUID;

import br.com.medflow.core.persistence.CommonRepository;
import br.com.medflow.entities.Consultorio;

/**
 * Repository da entidade {@link Consultorio}.
 */
public interface ConsultorioRepository extends CommonRepository<Consultorio, UUID> {
}

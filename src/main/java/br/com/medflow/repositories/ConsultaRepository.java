package br.com.medflow.repositories;

import java.util.UUID;

import br.com.medflow.core.persistence.CommonRepository;
import br.com.medflow.entities.Consulta;

/**
 * Repository da entidade {@link Consulta}.
 */
public interface ConsultaRepository extends CommonRepository<Consulta, UUID> {
}

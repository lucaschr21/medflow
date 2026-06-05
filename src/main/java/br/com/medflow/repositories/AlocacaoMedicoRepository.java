package br.com.medflow.repositories;

import java.util.UUID;

import br.com.medflow.core.persistence.CommonRepository;
import br.com.medflow.entities.AlocacaoMedico;

/**
 * Repository da entidade {@link AlocacaoMedico}.
 */
public interface AlocacaoMedicoRepository extends CommonRepository<AlocacaoMedico, UUID> {
}

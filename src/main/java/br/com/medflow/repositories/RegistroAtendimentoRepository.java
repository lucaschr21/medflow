package br.com.medflow.repositories;

import java.util.UUID;

import br.com.medflow.core.persistence.CommonRepository;
import br.com.medflow.entities.RegistroAtendimento;

/**
 * Repository da entidade {@link RegistroAtendimento}.
 */
public interface RegistroAtendimentoRepository extends CommonRepository<RegistroAtendimento, UUID> {
}

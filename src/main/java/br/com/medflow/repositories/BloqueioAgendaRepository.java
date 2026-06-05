package br.com.medflow.repositories;

import java.util.UUID;

import br.com.medflow.core.persistence.CommonRepository;
import br.com.medflow.entities.BloqueioAgenda;

/**
 * Repository da entidade {@link BloqueioAgenda}.
 */
public interface BloqueioAgendaRepository extends CommonRepository<BloqueioAgenda, UUID> {
}

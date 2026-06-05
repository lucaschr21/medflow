package br.com.medflow.repositories;

import java.util.UUID;

import br.com.medflow.core.persistence.CommonRepository;
import br.com.medflow.entities.Organizacao;

/**
 * Repository da entidade {@link Organizacao}.
 */
public interface OrganizacaoRepository extends CommonRepository<Organizacao, UUID> {
}

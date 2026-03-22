package br.com.medflow.repositories.estrutura;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.medflow.entities.estrutura.Unidade;

public interface UnidadeRepository extends JpaRepository<Unidade, UUID> {

  List<Unidade> findByOrganizacaoId(UUID organizacaoId);

  boolean existsByOrganizacaoId(UUID organizacaoId);
}

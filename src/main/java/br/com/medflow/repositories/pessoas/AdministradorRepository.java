package br.com.medflow.repositories.pessoas;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.medflow.entities.pessoas.Administrador;

public interface AdministradorRepository extends JpaRepository<Administrador, UUID> {

  Optional<Administrador> findByUtilizadorId(UUID utilizadorId);

  boolean existsByUtilizadorId(UUID utilizadorId);

  List<Administrador> findByOrganizacaoId(UUID organizacaoId);

  List<Administrador> findByUnidadeId(UUID unidadeId);
}

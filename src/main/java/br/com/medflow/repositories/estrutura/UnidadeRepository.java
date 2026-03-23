package br.com.medflow.repositories.estrutura;

import br.com.medflow.entities.estrutura.Unidade;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnidadeRepository extends JpaRepository<Unidade, UUID> {

  List<Unidade> findByOrganizacaoId(UUID organizacaoId);

  boolean existsByOrganizacaoId(UUID organizacaoId);
}

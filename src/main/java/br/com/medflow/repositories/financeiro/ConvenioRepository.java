package br.com.medflow.repositories.financeiro;

import br.com.medflow.entities.financeiro.Convenio;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConvenioRepository extends JpaRepository<Convenio, UUID> {

  List<Convenio> findByOrganizacaoId(UUID organizacaoId);

  List<Convenio> findByOrganizacaoIdAndAtivoTrue(UUID organizacaoId);

  boolean existsByOrganizacaoId(UUID organizacaoId);
}

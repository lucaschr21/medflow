package br.com.medflow.repositories.atendimento;

import br.com.medflow.entities.atendimento.ProcessoClinico;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessoClinicoRepository extends JpaRepository<ProcessoClinico, UUID> {

  Optional<ProcessoClinico> findByIdAndOrganizacaoId(UUID id, UUID organizacaoId);

  boolean existsByIdAndOrganizacaoId(UUID id, UUID organizacaoId);

  Optional<ProcessoClinico> findByIdAndPacienteOrganizacaoId(UUID id, UUID organizacaoId);

  boolean existsByIdAndPacienteOrganizacaoId(UUID id, UUID organizacaoId);

  Optional<ProcessoClinico> findByPacienteId(UUID pacienteId);

  boolean existsByPacienteId(UUID pacienteId);
}

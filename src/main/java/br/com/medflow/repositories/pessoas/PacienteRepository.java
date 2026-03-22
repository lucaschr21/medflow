package br.com.medflow.repositories.pessoas;

import br.com.medflow.entities.pessoas.Paciente;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, UUID> {

  Optional<Paciente> findByIdAndOrganizacaoId(UUID id, UUID organizacaoId);

  boolean existsByIdAndOrganizacaoId(UUID id, UUID organizacaoId);

  Optional<Paciente> findByUtilizadorId(UUID utilizadorId);

  boolean existsByUtilizadorId(UUID utilizadorId);

  Optional<Paciente> findByProcessoClinicoId(UUID processoClinicoId);

  boolean existsByProcessoClinicoId(UUID processoClinicoId);
}

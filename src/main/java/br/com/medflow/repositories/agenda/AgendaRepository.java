package br.com.medflow.repositories.agenda;

import br.com.medflow.entities.agenda.Agenda;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendaRepository extends JpaRepository<Agenda, UUID> {

  Optional<Agenda> findByIdAndVinculacaoConsultorioMedicoConsultorioUnidadeOrganizacaoId(
      UUID id, UUID organizacaoId);

  Optional<Agenda> findByVinculacaoConsultorioMedicoId(UUID consultorioMedicoId);

  boolean existsByVinculacaoConsultorioMedicoId(UUID consultorioMedicoId);
}

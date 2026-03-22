package br.com.medflow.repositories.estrutura;

import br.com.medflow.entities.estrutura.ConsultorioMedico;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultorioMedicoRepository extends JpaRepository<ConsultorioMedico, UUID> {

  Optional<ConsultorioMedico> findByIdAndConsultorioUnidadeOrganizacaoId(
      UUID id, UUID organizacaoId);

  Optional<ConsultorioMedico> findByConsultorioIdAndMedicoId(UUID consultorioId, UUID medicoId);

  boolean existsByConsultorioIdAndMedicoId(UUID consultorioId, UUID medicoId);

  List<ConsultorioMedico> findByConsultorioId(UUID consultorioId);

  List<ConsultorioMedico> findByConsultorioUnidadeOrganizacaoId(UUID organizacaoId);

  List<ConsultorioMedico> findByMedicoIdAndConsultorioUnidadeOrganizacaoId(
      UUID medicoId, UUID organizacaoId);

  List<ConsultorioMedico> findByMedicoId(UUID medicoId);
}

package br.com.medflow.repositories.atendimento;

import br.com.medflow.entities.atendimento.Consulta;
import br.com.medflow.entities.atendimento.EstadoConsulta;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultaRepository extends JpaRepository<Consulta, UUID> {

  Optional<Consulta> findByIdAndPacienteOrganizacaoId(UUID id, UUID organizacaoId);

  boolean existsByIdAndPacienteOrganizacaoId(UUID id, UUID organizacaoId);

  boolean existsByMedicoIdAndDataHoraMarcacaoAndEstadoNot(
      UUID medicoId, LocalDateTime dataHoraMarcacao, EstadoConsulta estado);

  boolean existsByConsultorioIdAndDataHoraMarcacaoAndEstadoNot(
      UUID consultorioId, LocalDateTime dataHoraMarcacao, EstadoConsulta estado);
}

package br.com.medflow.repositories.atendimento;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.medflow.entities.atendimento.Consulta;
import br.com.medflow.entities.atendimento.EstadoConsulta;

public interface ConsultaRepository extends JpaRepository<Consulta, UUID> {

  boolean existsByMedicoIdAndDataHoraMarcacaoAndEstadoNot(
      UUID medicoId, LocalDateTime dataHoraMarcacao, EstadoConsulta estado);

  boolean existsByConsultorioIdAndDataHoraMarcacaoAndEstadoNot(
      UUID consultorioId, LocalDateTime dataHoraMarcacao, EstadoConsulta estado);
}

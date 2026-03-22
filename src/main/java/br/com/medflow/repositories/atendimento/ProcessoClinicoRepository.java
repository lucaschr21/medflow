package br.com.medflow.repositories.atendimento;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.medflow.entities.atendimento.ProcessoClinico;

public interface ProcessoClinicoRepository extends JpaRepository<ProcessoClinico, UUID> {

  Optional<ProcessoClinico> findByPacienteId(UUID pacienteId);

  boolean existsByPacienteId(UUID pacienteId);
}

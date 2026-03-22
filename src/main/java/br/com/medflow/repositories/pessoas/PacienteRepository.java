package br.com.medflow.repositories.pessoas;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.medflow.entities.pessoas.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, UUID> {

  Optional<Paciente> findByUtilizadorId(UUID utilizadorId);

  boolean existsByUtilizadorId(UUID utilizadorId);

  Optional<Paciente> findByProcessoClinicoId(UUID processoClinicoId);

  boolean existsByProcessoClinicoId(UUID processoClinicoId);
}
